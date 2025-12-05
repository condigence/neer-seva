import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ProductsModel } from '../../model/products.model';
import { Router } from '@angular/router';
import { StorageService } from '../../services/storage.service';
import { OrderService } from '../../services/order.service';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../services/auth.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.pages.html',
  styleUrls: ['./payment.pages.scss'] 
})

export class PaymentPage implements OnInit {
  public cartflag: boolean = false;
  public selectedPaymentMethod: string = '';
  public upiId: string = '';
  public upiIdError: string = '';
  public isProcessing: boolean = false;
  // Replace with your own QR code image path or UPI payment URL
  // Option 1: Use static QR code image from assets
  // public qrCodeUrl: string = 'assets/images/upi-qr-code.png';
  // Option 2: Generate QR code dynamically with your UPI ID
  public qrCodeUrl: string = 'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=upi://pay?pa=yourmerchant@paytm&pn=NeerSeva&am=';
  private currentUser: any;
  private order: any;

  constructor(
    public cart: CartService,
    public products: ProductsModel,
    private router: Router,
    private storage: StorageService,
    private orderService: OrderService,
    private toastr: ToastrService,
    private http: HttpClient,
    private authService: AuthenticationService
  ) {
    // Get current user
    this.authService.currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnInit() {
    this.ref();
    // Update QR code with cart total (only if using dynamic QR generation)
    if (this.cart.cartTotal && this.qrCodeUrl.includes('qrserver.com')) {
      this.qrCodeUrl += this.cart.cartTotal;
    }
  }

  ref() {
    this.cartflag = false;
    setTimeout(() => {
      this.cartflag = true;
    }, 100);
  }

  selectPaymentMethod(method: string) {
    this.selectedPaymentMethod = method;
    this.upiIdError = '';
  }

  goBack() {
    this.router.navigate(['/billing']);
  }

  validateUpiId(): boolean {
    if (!this.upiId || this.upiId.trim() === '') {
      this.upiIdError = 'Please enter your UPI ID';
      return false;
    }
    
    // Basic UPI ID validation
    const upiRegex = /^[\w.-]+@[\w.-]+$/;
    if (!upiRegex.test(this.upiId)) {
      this.upiIdError = 'Please enter a valid UPI ID (e.g., yourname@upi)';
      return false;
    }
    
    this.upiIdError = '';
    return true;
  }

  confirmPayment() {
    // Validate based on payment method
    if (this.selectedPaymentMethod === 'upi') {
      if (!this.validateUpiId()) {
        return;
      }
      // Store UPI ID for reference
      this.storage.set({ upiId: this.upiId });
    }

    // For COD, proceed directly to place order
    if (this.selectedPaymentMethod === 'cod') {
      this.placeOrder();
    } else if (this.selectedPaymentMethod === 'upi') {
      // For UPI, simulate payment success and then place order
      this.toastr.info('Processing UPI payment...', 'Please wait');
      setTimeout(() => {
        this.toastr.success('Payment successful!', 'Success');
        this.placeOrder();
      }, 1500);
    }
  }

  placeOrder() {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.isProcessing = true;

    let currentUser = localStorage.getItem('currentUser');
    let custId = JSON.parse(currentUser).id;

    // Get current customer
    let customer = { "customerId": custId };

    const prepareAndPlace = (shopIdValue: any) => {
      const shop = { id: Number(shopIdValue) };

      let stockItems = [];
      for (let i = 0; i < this.cart.cartItemsList.length; i++) {
        const cartItem = this.cart.cartItemsList[i];
        const stockId = cartItem.id; // stock identifier from catalog
        const itemId = cartItem.itemId ?? null; // underlying product id
        let stockItem = { stockId: stockId, itemId: itemId, quantity: cartItem.qty };
        stockItems.push(stockItem);
      }

      console.log('Preparing to place order for customer:', customer, 'shop:', shop, 'items:', stockItems);
      // prepare Order data with payment info
      this.order = { 
        customer: customer, 
        shop: shop, 
        stockItems: stockItems,
        paymentMethod: this.selectedPaymentMethod,
        paymentStatus: this.selectedPaymentMethod === 'cod' ? 'pending' : 'completed'
      };
      
      console.log('Payments Page. Placing order with data:', this.order);

      try {
        this.orderService.placeMyOrder(this.order)
          .subscribe(
            data => {
              console.log('Order placed successfully:', data);
              this.isProcessing = false;
              this.toastr.success('Order placed successfully! Redirecting...', 'Success');
              // Navigate to checkout success page
              setTimeout(() => {
                this.router.navigate(['/checkout']);
              }, 500);
            },
            error => {
              console.error('Error placing order:', error);
              this.isProcessing = false;

              let errorMessage = 'There was an error placing your order. Please try again.';
              if (error.error && error.error.message) {
                errorMessage = error.error.message;
              } else if (error.message) {
                errorMessage = error.message;
              }

              this.toastr.error(errorMessage, 'Order Failed');
            }
          );
      } catch (e) {
        console.error('Exception while placing order:', e);
        this.isProcessing = false;
        this.toastr.error('An unexpected error occurred. Please try again.', 'Error');
      }
    };

    // Get Selected vendor from localStorage; if not present, fetch shops and pick first
    let storedShop = localStorage.getItem('selectedShop');
    if (storedShop && storedShop !== 'undefined' && storedShop !== 'null') {
      prepareAndPlace(storedShop);
    } else {
      // fetch shops and pick the first one
      const shopsUrl = 'http://localhost:9093/neerseva/api/v1/stocks/shops';
      this.http.get<any[]>(shopsUrl).subscribe(
        (shops) => {
          if (shops && shops.length > 0) {
            const first = shops[0];
            const firstId = first.id ?? first.shopId ?? first.shop_id ?? first._id ?? null;
            if (firstId !== null && firstId !== undefined) {
              localStorage.setItem('selectedShop', String(firstId));
              prepareAndPlace(firstId);
            } else {
              console.error('No id found on first shop item', first);
              this.isProcessing = false;
              this.toastr.error('Unable to find shop information. Please try again.', 'Error');
            }
          } else {
            console.error('No shops returned from', shopsUrl);
            this.isProcessing = false;
            this.toastr.error('No shops available. Please contact support.', 'Error');
          }
        },
        (err) => {
          console.error('Failed to load shops for default selection', err);
          this.isProcessing = false;
          this.toastr.error('Failed to load shop information. Please try again.', 'Error');
        }
      );
    }
  }
}
