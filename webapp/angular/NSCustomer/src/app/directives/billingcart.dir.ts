import { Component } from '@angular/core';
import { CartService } from '../services/cart.service';
@Component({
  selector :'billing-cart',
  template : `
    <div class="cart-summary-card">
      <div class="cart-header">
        <h5 class="cart-title">
          <i class="zmdi zmdi-shopping-basket mr-2"></i>Order Summary
        </h5>
        <span class="items-badge">{{cart.cartItemsList.length}} Items</span>
      </div>

      <div class="cart-items-list">
        <div class="cart-item" *ngFor="let itm of cart.cartItemsList">
          <div class="item-details">
            <h6 class="item-name">{{itm.name}}</h6>
            <div class="item-meta">
              <span class="quantity-badge">
                <i class="zmdi zmdi-collection-item mr-1"></i>{{itm.qty}} × &#x20B9;{{itm.price/itm.qty}}
              </span>
            </div>
          </div>
          <div class="item-price">
            &#x20B9;{{itm.price}}
          </div>
        </div>
      </div>

      <div class="cart-total">
        <div class="total-row">
          <span class="total-label">Total Amount</span>
          <span class="total-amount">&#x20B9;{{cart.cartTotal}}</span>
        </div>
      </div>
    </div>
  `,
  styles : [`
    .cart-summary-card {
      background: #ffffff;
      border-radius: 15px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      animation: slideIn 0.5s ease-out;
      position: sticky;
      top: 100px;
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
      font-size: 1.4rem;
    }

    .items-badge {
      background: rgba(255, 255, 255, 0.3);
      backdrop-filter: blur(10px);
      color: #ffffff;
      padding: 6px 16px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 0.9rem;
      border: 2px solid rgba(255, 255, 255, 0.4);
    }

    .cart-items-list {
      padding: 20px;
      max-height: 400px;
      overflow-y: auto;
    }

    .cart-items-list::-webkit-scrollbar {
      width: 6px;
    }

    .cart-items-list::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 10px;
    }

    .cart-items-list::-webkit-scrollbar-thumb {
      background: #667eea;
      border-radius: 10px;
    }

    .cart-item {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 15px;
      margin-bottom: 12px;
      background: #f7fafc;
      border-radius: 10px;
      transition: all 0.3s ease;
      border: 2px solid transparent;
    }

    .cart-item:hover {
      background: #edf2f7;
      border-color: #667eea;
      transform: translateX(5px);
    }

    .cart-item:last-child {
      margin-bottom: 0;
    }

    .item-details {
      flex: 1;
      margin-right: 10px;
    }

    .item-name {
      font-size: 1rem;
      font-weight: 600;
      color: #2d3748;
      margin: 0 0 8px 0;
      line-height: 1.4;
    }

    .item-meta {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .quantity-badge {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 4px 12px;
      border-radius: 15px;
      font-size: 0.85rem;
      font-weight: 600;
      display: inline-flex;
      align-items: center;
    }

    .quantity-badge i {
      font-size: 0.9rem;
    }

    .item-price {
      font-size: 1.2rem;
      font-weight: 700;
      color: #667eea;
      white-space: nowrap;
    }

    .cart-total {
      background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
      padding: 20px;
      border-top: 2px solid #e2e8f0;
    }

    .total-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
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

    /* Responsive */
    @media (max-width: 991px) {
      .cart-summary-card {
        position: static;
        margin-bottom: 20px;
      }
    }
  `]
})

export class BillingCartDir{

  constructor(
    public cart: CartService
  ){
  }

  ngOnInit() {
    // Force cart to reload from localStorage when component initializes
    this.cart.loadCart();
    console.log('Billing cart initialized, items:', this.cart.cartItemsList.length);
  }

}
