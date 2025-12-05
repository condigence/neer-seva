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
  template:`
  <menu></menu>
  <div class="content-wrapper">
    <div class="container-fluid px-0">
      <div class="row no-gutters justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <div class="card payment-card shadow-lg border-0">
            <div class="card-header bg-gradient text-white">
              <h4 class="mb-0">
                <i class="zmdi zmdi-card mr-2"></i>Payment Options
              </h4>
            </div>
            <div class="card-body p-4">
              
              <!-- Payment Method Selection -->
              <div class="payment-methods mb-4">
                <h5 class="section-title mb-3">Choose Payment Method</h5>
                
                <div class="payment-option" [class.active]="selectedPaymentMethod === 'upi'" 
                     (click)="selectPaymentMethod('upi')">
                  <div class="option-radio">
                    <input type="radio" name="payment" id="upi" [checked]="selectedPaymentMethod === 'upi'">
                    <label for="upi"></label>
                  </div>
                  <div class="option-content">
                    <i class="zmdi zmdi-smartphone-android payment-icon"></i>
                    <div class="option-details">
                      <h6>UPI Payment</h6>
                      <p>Pay using UPI ID or QR Code</p>
                    </div>
                  </div>
                </div>

                <div class="payment-option" [class.active]="selectedPaymentMethod === 'cod'" 
                     (click)="selectPaymentMethod('cod')">
                  <div class="option-radio">
                    <input type="radio" name="payment" id="cod" [checked]="selectedPaymentMethod === 'cod'">
                    <label for="cod"></label>
                  </div>
                  <div class="option-content">
                    <i class="zmdi zmdi-money payment-icon"></i>
                    <div class="option-details">
                      <h6>Cash on Delivery</h6>
                      <p>Pay when you receive the order</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- UPI Payment Details -->
              <div *ngIf="selectedPaymentMethod === 'upi'" class="upi-section animate-in">
                <div class="row">
                  <div class="col-md-6 mb-4">
                    <div class="qr-code-container">
                      <h6 class="text-center mb-3">Scan QR Code</h6>
                      <div class="qr-code-wrapper">
                        <img [src]="qrCodeUrl" alt="UPI QR Code" class="qr-code-image">
                      </div>
                      <p class="text-center mt-2 small text-muted">Scan with any UPI app</p>
                    </div>
                  </div>
                  
                  <div class="col-md-6">
                    <div class="upi-input-section">
                      <h6 class="mb-3">Or Enter UPI ID</h6>
                      <div class="form-group">
                        <label>UPI ID</label>
                        <input type="text" 
                               class="form-control" 
                               [(ngModel)]="upiId"
                               placeholder="yourname@upi"
                               [class.is-invalid]="upiIdError">
                        <div class="invalid-feedback" *ngIf="upiIdError">
                          <i class="zmdi zmdi-alert-circle-o mr-1"></i>{{upiIdError}}
                        </div>
                      </div>
                      
                      <div class="payment-amount-display">
                        <div class="d-flex justify-content-between align-items-center">
                          <span class="text-muted">Amount to Pay:</span>
                          <span class="amount">₹{{cart.cartTotal || 0}}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- COD Confirmation -->
              <div *ngIf="selectedPaymentMethod === 'cod'" class="cod-section animate-in">
                <div class="alert alert-info">
                  <i class="zmdi zmdi-info mr-2"></i>
                  <strong>Cash on Delivery Selected</strong>
                  <p class="mb-0 mt-2">Please keep exact amount ready. Amount to be paid: <strong>₹{{cart.cartTotal || 0}}</strong></p>
                </div>
              </div>

              <!-- Action Buttons -->
              <div class="payment-actions mt-4">
                <button class="btn btn-secondary" (click)="goBack()" [disabled]="isProcessing">
                  <i class="zmdi zmdi-arrow-left mr-2"></i>Back to Billing
                </button>
                
                <button class="btn btn-primary" 
                        (click)="confirmPayment()" 
                        [disabled]="!selectedPaymentMethod || isProcessing">
                  <span *ngIf="!isProcessing">
                    <i class="zmdi zmdi-check-circle mr-2"></i>Confirm Payment
                  </span>
                  <span *ngIf="isProcessing">
                    <span class="spinner-border spinner-border-sm mr-2"></span>
                    Processing...
                  </span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  `,
  styles: [`
    .content-wrapper {
      min-height: 100vh;
      padding: 100px 0 30px;
      background: #f5f7fa;
      width: 100%;
      margin: 0;
    }

    .container-fluid {
      padding: 0 15px;
    }

    .payment-card {
      border-radius: 15px;
      overflow: hidden;
      animation: slideIn 0.5s ease-out;
    }

    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .bg-gradient {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
      padding: 20px 25px;
    }

    .bg-gradient h4 {
      font-weight: 700;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
    }

    .section-title {
      font-weight: 600;
      color: #2d3748;
      border-bottom: 2px solid #e2e8f0;
      padding-bottom: 10px;
    }

    .payment-methods {
      margin-top: 20px;
    }

    .payment-option {
      display: flex;
      align-items: center;
      padding: 20px;
      border: 2px solid #e2e8f0;
      border-radius: 12px;
      margin-bottom: 15px;
      cursor: pointer;
      transition: all 0.3s ease;
      background: white;
    }

    .payment-option:hover {
      border-color: #667eea;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
      transform: translateY(-2px);
    }

    .payment-option.active {
      border-color: #667eea;
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
    }

    .option-radio {
      margin-right: 15px;
    }

    .option-radio input[type="radio"] {
      width: 20px;
      height: 20px;
      cursor: pointer;
    }

    .option-content {
      display: flex;
      align-items: center;
      flex: 1;
    }

    .payment-icon {
      font-size: 2.5rem;
      color: #667eea;
      margin-right: 15px;
    }

    .option-details h6 {
      margin: 0;
      font-weight: 600;
      color: #2d3748;
    }

    .option-details p {
      margin: 5px 0 0;
      color: #718096;
      font-size: 0.9rem;
    }

    .upi-section, .cod-section {
      padding: 20px;
      background: #f7fafc;
      border-radius: 12px;
      margin-top: 20px;
    }

    .animate-in {
      animation: fadeIn 0.4s ease-in;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
        transform: translateY(10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .qr-code-container {
      text-align: center;
    }

    .qr-code-wrapper {
      background: white;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      display: inline-block;
    }

    .qr-code-image {
      width: 200px;
      height: 200px;
      border-radius: 8px;
    }

    .upi-input-section h6 {
      font-weight: 600;
      color: #2d3748;
    }

    .form-group label {
      font-weight: 600;
      color: #4a5568;
      margin-bottom: 8px;
      display: block;
    }

    .form-control {
      border: 2px solid #e2e8f0;
      border-radius: 10px;
      padding: 12px 15px;
      font-size: 0.95rem;
      transition: all 0.3s ease;
    }

    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
      outline: none;
    }

    .form-control.is-invalid {
      border-color: #fc8181;
      background-color: #fff5f5;
    }

    .invalid-feedback {
      display: block;
      color: #e53e3e;
      font-size: 0.875rem;
      margin-top: 8px;
    }

    .payment-amount-display {
      background: white;
      padding: 15px;
      border-radius: 10px;
      margin-top: 20px;
      border-left: 4px solid #667eea;
    }

    .payment-amount-display .amount {
      font-size: 1.5rem;
      font-weight: 700;
      color: #667eea;
    }

    .cod-section .alert {
      border-radius: 10px;
      border: none;
      background: linear-gradient(135deg, #e0f2fe 0%, #dbeafe 100%);
      color: #1e40af;
    }

    .payment-actions {
      display: flex;
      justify-content: space-between;
      gap: 15px;
      padding-top: 20px;
      border-top: 2px solid #e2e8f0;
    }

    .btn {
      padding: 12px 30px;
      font-weight: 600;
      border-radius: 10px;
      transition: all 0.3s ease;
      border: none;
      cursor: pointer;
      font-size: 1rem;
    }

    .btn-secondary {
      background: #e2e8f0;
      color: #4a5568;
    }

    .btn-secondary:hover:not(:disabled) {
      background: #cbd5e0;
      transform: translateY(-2px);
    }

    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
    }

    .btn-primary:hover:not(:disabled) {
      background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 25px rgba(102, 126, 234, 0.4);
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      transform: none;
    }

    .spinner-border-sm {
      width: 1rem;
      height: 1rem;
    }

    /* Responsive Styles */
    @media (max-width: 991px) {
      .content-wrapper {
        padding: 80px 0 25px;
      }

      .container-fluid {
        padding: 0 12px;
      }

      .card-body {
        padding: 15px;
      }

      .bg-gradient {
        padding: 15px 20px;
      }
    }

    @media (max-width: 768px) {
      .content-wrapper {
        padding: 75px 0 20px;
      }

      .container-fluid {
        padding: 0 10px;
      }

      .payment-actions {
        flex-direction: column;
      }
      
      .btn {
        width: 100%;
      }

      .payment-option {
        padding: 15px;
      }

      .card-body {
        padding: 12px;
      }

      .bg-gradient {
        padding: 12px 15px;
      }

      .bg-gradient h4 {
        font-size: 1.1rem;
      }
    }

    @media (max-width: 480px) {
      .content-wrapper {
        padding: 70px 0 15px;
      }

      .container-fluid {
        padding: 0 8px;
      }

      .card-body {
        padding: 10px;
      }

      .payment-option {
        padding: 12px;
      }

      .bg-gradient {
        padding: 10px 12px;
      }

      .bg-gradient h4 {
        font-size: 1rem;
      }

      .qr-code-image {
        width: 150px;
        height: 150px;
      }
    }
  `]
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
