import { Component } from '@angular/core';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { AddressService } from '../services/address.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector :'cart',
  template : `
  <div class="card text-center">
  <div class="card-header">
    Your Cart Items
    <button type="button" class="btn btn-sm btn-warning float-right">
        Total items <span class="badge badge-dark">{{cart.cartItemsList.length}}</span>
    </button>
    <button type="button" class="btn btn-sm btn-danger mr-2 float-right" 
            (click)="openEmptyCartModal(emptyCartModal)" 
            [disabled]="!cart.cartItemsList || cart.cartItemsList.length === 0">
       <i class="zmdi zmdi-shopping-cart mr-1"></i>Empty Cart
    </button>
  </div>
  <div class="card-body">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Product</th>
          <th scope="col">Price</th>
          <th scope="col">Qty</th>
          <th scope="col">Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>

        </tr>
        <tr *ngFor="let item of cart.cartItemsList ">
          <td class="text-left">{{item.name}}</td>
          <td>{{item.price/item.qty}} x</td>
          <td class="w30">
            <div class="input-group input-group-sm mb-3">
              <div class="input-group-prepend">
                <button class="btn btn-info" type="button" (click)="changeQty(item.id,1,'')">+</button>
              </div>
              <input type="text" class="form-control text-center" value="{{item.qty}}"
               #qtyRef (keyup)="changeQty(item.id,qtyRef.value,'replace')"
               >
              <div class="input-group-append">
                <button class="btn btn-danger" type="button" (click)="changeQty(item.id,-1,'')">-</button>
              </div>
            </div>
          </td>
          <td>{{item.price}}</td>
        </tr>
        </tbody>
    </table>
    <a (click)="goToCheckout()" class="btn btn-sm btn-primary mt-3" *ngIf="cart.cartItemsList && cart.cartTotal">Checkout</a>
  </div>
  <div class="card-footer text-muted font-weight-bold">
    Total : Rs. {{cart.cartTotal}}
  </div>
</div>

<!-- Empty Cart Confirmation Modal -->
<ng-template #emptyCartModal let-modal>
  <div class="modal-header bg-danger text-white">
    <h4 class="modal-title" id="modal-title">
      <i class="zmdi zmdi-alert-triangle mr-2"></i>Clear Cart?
    </h4>
    <button type="button" class="close text-white" aria-label="Close" (click)="modal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body text-center p-4">
    <div class="mb-3">
      <i class="zmdi zmdi-shopping-cart zmdi-hc-3x text-danger mb-3"></i>
    </div>
    <h5 class="mb-3">Are you sure you want to clear your cart?</h5>
    <p class="text-muted">This will remove all items from your cart. This action cannot be undone.</p>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-outline-secondary" (click)="modal.dismiss('Cancel')">
      <i class="zmdi zmdi-close mr-1"></i>Cancel
    </button>
    <button type="button" class="btn btn-danger" (click)="modal.close('Confirm')">
      <i class="zmdi zmdi-delete mr-1"></i>Yes, Clear Cart
    </button>
  </div>
</ng-template>
  `,
  styles : [`
  .table td {
    vertical-align : unset;
    font-size:14px;
  }
  .w30{
    width: 35%;
  }
  `]
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

  changeQty(id, qty, replace) {
    if (qty !== '') {
      qty = parseInt(qty) || 1;
      this.cart.addToCart(id, qty, replace);
    } else {
      this.cart.addToCart(id, 1, replace);
    }
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
