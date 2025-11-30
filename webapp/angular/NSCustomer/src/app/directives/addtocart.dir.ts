import { Component } from '@angular/core';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { AddressService } from '../services/address.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector :'cart',
  template : `
  <div class="cart-card">
    <div class="cart-header">
      <h5 class="cart-title">
        <i class="zmdi zmdi-shopping-cart mr-2"></i>Your Cart
      </h5>
      <div class="header-actions">
        <button type="button" class="btn-total-items">
          <i class="zmdi zmdi-receipt mr-1"></i>
          <span class="items-count">{{itemsCount()}}</span>
          <span class="items-label">Items</span>
        </button>
        <button type="button" class="btn-empty-cart" 
                (click)="openEmptyCartModal(emptyCartModal)" 
                [disabled]="!hasItems()">
          <i class="zmdi zmdi-delete mr-1"></i>Clear
        </button>
      </div>
    </div>
    
    <div class="cart-body">
      <div class="empty-cart-state" *ngIf="!hasItems()">
        <i class="zmdi zmdi-shopping-cart-plus zmdi-hc-5x text-muted mb-3"></i>
        <h5 class="text-muted">Your cart is empty</h5>
        <p class="text-muted">Add some products to get started!</p>
      </div>
      
      <div class="cart-items" *ngIf="hasItems()">
        <div class="cart-item" *ngFor="let item of cart.cartItemsList">
          <div class="item-thumb" *ngIf="item.image">
            <img [src]="'data:image/jpeg;base64,' + item.image" alt="{{item.name}}" />
          </div>
          <div class="item-info">
            <h6 class="item-name">{{item.name}}</h6>
            <p class="item-price">&#x20B9;{{unitPrice(item) | number:'1.2-2'}} each</p>
          </div>
          <div class="item-controls">
            <div class="qty-controls">
              <button class="qty-btn qty-minus" type="button" (click)="changeQty(item.id,-1,'')">
                <i class="zmdi zmdi-minus"></i>
              </button>
              <input type="text" class="qty-input" [value]="item.qty"
                     #qtyRef (keyup)="changeQty(item.id,qtyRef.value,'replace')">
              <button class="qty-btn qty-plus" type="button" (click)="changeQty(item.id,1,'')">
                <i class="zmdi zmdi-plus"></i>
              </button>
            </div>
            <div class="item-total">
              &#x20B9;{{item.price | number:'1.2-2'}}
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="cart-footer" *ngIf="hasItems()">
      <div class="total-section">
        <span class="total-label">Total Amount</span>
        <span class="total-amount">&#x20B9;{{(cart.cartTotal || 0) | number:'1.2-2'}}</span>
      </div>
      <button class="btn-checkout" (click)="goToCheckout()">
        <i class="zmdi zmdi-check mr-2"></i>Proceed to Checkout
      </button>
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
    .cart-card {
      background: #ffffff;
      border-radius: 15px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      animation: slideIn 0.3s ease-out;
    }

    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateY(20px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .cart-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .cart-title {
      color: #ffffff;
      font-weight: 700;
      font-size: 1.3rem;
      margin: 0;
      display: flex;
      align-items: center;
    }

    .cart-title i {
      font-size: 1.5rem;
    }

    .header-actions {
      display: flex;
      gap: 10px;
      align-items: center;
    }

    .btn-total-items {
      background: rgba(255, 255, 255, 0.2);
      backdrop-filter: blur(10px);
      border: 2px solid rgba(255, 255, 255, 0.3);
      color: #ffffff;
      padding: 8px 16px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 0.9rem;
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: default;
      transition: all 0.3s ease;
    }

    .btn-total-items:hover {
      background: rgba(255, 255, 255, 0.3);
      transform: scale(1.05);
    }

    .items-count {
      background: #ffffff;
      color: #667eea;
      padding: 4px 10px;
      border-radius: 12px;
      font-weight: 700;
      font-size: 1rem;
    }

    .items-label {
      font-size: 0.85rem;
    }

    .btn-empty-cart {
      background: rgba(255, 107, 107, 0.9);
      border: none;
      color: #ffffff;
      padding: 8px 16px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 0.9rem;
      cursor: pointer;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
    }

    .btn-empty-cart:hover:not(:disabled) {
      background: #ff5252;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(255, 82, 82, 0.4);
    }

    .btn-empty-cart:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .cart-body {
      padding: 20px;
      min-height: 200px;
    }

    .empty-cart-state {
      text-align: center;
      padding: 40px 20px;
    }

    .empty-cart-state i {
      color: #cbd5e0;
    }

    .cart-items {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .cart-item {
      background: #f7fafc;
      border-radius: 12px;
      padding: 15px;
      display: flex;
      align-items: center;
      gap: 12px;
      transition: all 0.3s ease;
      border: 2px solid transparent;
    }

    .cart-item:hover {
      background: #edf2f7;
      border-color: #667eea;
      transform: translateX(5px);
    }

    .item-info {
      flex: 1 1 auto;
      min-width: 0; /* allow text truncation instead of pushing layout */
    }

    .item-thumb {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      overflow: hidden;
      background: #fff;
      box-shadow: 0 2px 6px rgba(0,0,0,0.08);
      display: flex;
      align-items: center;
      justify-content: center;
      flex: 0 0 48px; /* keep thumbnail visible and non-shrinking */
    }

    .item-thumb img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .item-name {
      font-size: 1rem;
      font-weight: 600;
      color: #2d3748;
      margin: 0 0 5px 0;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden; /* prevent long names from pushing the thumbnail */
    }

    .item-price {
      font-size: 0.85rem;
      color: #718096;
      margin: 0;
    }

    .item-controls {
      display: flex;
      align-items: center;
      gap: 15px;
      margin-left: auto; /* push controls to the right, keep thumb visible */
    }

    .qty-controls {
      display: flex;
      align-items: center;
      gap: 8px;
      background: #ffffff;
      border-radius: 25px;
      padding: 4px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .qty-btn {
      width: 32px;
      height: 32px;
      border: none;
      border-radius: 50%;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s ease;
      font-size: 1rem;
    }

    .qty-minus {
      background: linear-gradient(135deg, #FF6B6B 0%, #EE5A6F 100%);
      color: white;
    }

    .qty-minus:hover {
      transform: scale(1.1);
      box-shadow: 0 3px 10px rgba(255, 107, 107, 0.4);
    }

    .qty-plus {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }

    .qty-plus:hover {
      transform: scale(1.1);
      box-shadow: 0 3px 10px rgba(102, 126, 234, 0.4);
    }

    .qty-input {
      width: 50px;
      text-align: center;
      border: none;
      background: transparent;
      font-weight: 600;
      font-size: 1rem;
      color: #2d3748;
      outline: none;
    }

    .item-total {
      font-size: 1.2rem;
      font-weight: 700;
      color: #667eea;
      min-width: 80px;
      text-align: right;
    }

    .cart-footer {
      background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
      padding: 20px;
      border-top: 2px solid #e2e8f0;
    }

    .total-section {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
      padding: 15px;
      background: #ffffff;
      border-radius: 10px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .total-label {
      font-size: 1.1rem;
      font-weight: 600;
      color: #4a5568;
    }

    .total-amount {
      font-size: 1.8rem;
      font-weight: 700;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .btn-checkout {
      width: 100%;
      padding: 15px 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 12px;
      font-weight: 600;
      font-size: 1.1rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn-checkout:hover {
      background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 25px rgba(102, 126, 234, 0.4);
    }

    .btn-checkout:active {
      transform: translateY(0);
    }

    .btn-checkout i {
      font-size: 1.3rem;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .cart-header {
        flex-direction: column;
        gap: 15px;
        align-items: flex-start;
      }

      .cart-item {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }

      .item-thumb {
        width: 56px;
        height: 56px;
      }

      .item-controls {
        width: 100%;
        justify-content: space-between;
      }
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

  changeQty(id: any, qty: any, replace: string) {
    if (replace === 'replace') {
      let parsed = parseInt(qty, 10);
      if (isNaN(parsed) || parsed < 1) {
        parsed = 1;
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
