import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddressService } from '../../services/address.service';
import { AuthenticationService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

@Component({
    selector :'cart',
    templateUrl: './addtocart.dir.html',
    styleUrls: ['./addtocart.dir.scss']
})


export class AddToCartDir{
  currentUser: any;

  constructor(
    public cart: CartService,
    public router: Router,
    private authenticationService: AuthenticationService,
    private addressService: AddressService,
    private modalService: NgbModal
  ) {  
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit() {
    //this.cart.addToCart(id, qty, replace);
  }

  changeQty(id: any, qty: any, replace: string) {
    if (replace === 'replace') {
      let parsed = parseInt(qty, 10);
      if (isNaN(parsed) || parsed < 1) {
        parsed = 1;
      }
      // Get available stock
      const availableStock = this.cart.getAvailableStock(id);
      if (availableStock !== null && parsed > availableStock) {
        parsed = availableStock;
      }
      this.cart.addToCart(id, parsed, 'replace');
      return;
    }

    const delta = parseInt(qty, 10);
    if (isNaN(delta)) {
      return;
    }
    this.cart.addToCart(id, delta, '');
  }

  itemsCount(): number {
    if (this.cart && Array.isArray(this.cart.cartItemsList) && this.cart.cartItemsList.length > 0) {
      return this.cart.cartItemsList.length;
    }
    try {
      const data = this.cart && this.cart.cartData ? this.cart.cartData : {};
      return Object.keys(data).length;
    } catch {
      return 0;
    }
  }

  hasItems(): boolean {
    if (this.cart && Array.isArray(this.cart.cartItemsList) && this.cart.cartItemsList.length > 0) {
      return true;
    }
    try {
      const data = this.cart && this.cart.cartData ? this.cart.cartData : {};
      return Object.keys(data).length > 0;
    } catch {
      return false;
    }
  }

  unitPrice(item: any): number {
    console.log('unitPrice called with item:', item);
    if (!item) { return 0; }
    const total = Number(item.price) || 0;
    const qty = Number(item.qty) || 0;
    if (qty <= 0) { return total; }
    return total / qty;
  }

  openEmptyCartModal(content: any) {
    this.modalService.open(content, { 
      centered: true,
      size: 'md',
      backdrop: 'static'
    }).result.then(
      (result) => {
        if (result === 'Confirm') {
          this.cart.emptyCart();
          // Use Angular router instead of full page reload
          this.router.navigate(['/products']);
        }
      },
      (reason) => {
        // Modal dismissed
      }
    );
  }

  goToCheckout() {
    // Check if user is logged in
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    // Check if user has address
    const userId = this.currentUser.id;
    this.addressService.getAddressByUserId(userId).subscribe(
      (addresses: any) => {
        // If user has at least one address, go to billing page
        if (addresses && (Array.isArray(addresses) ? addresses.length > 0 : addresses)) {
          this.router.navigate(['/billing']);
        } else {
          // No address found, redirect to add address page
          this.router.navigate(['address/add-address']);
        }
      },
      (error) => {
        // On error (e.g., no addresses found), redirect to add address page
        this.router.navigate(['/add-address']);
      }
    );
  }

}
