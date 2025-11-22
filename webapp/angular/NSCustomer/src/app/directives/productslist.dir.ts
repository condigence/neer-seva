import { Component, Input, Output, EventEmitter } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';



@Component({
  selector: 'productslist-dir',
  template: `
  
  <div class="row product-grid" >
    <div class="col-lg-4 col-md-6 col-sm-6 mb-4" *ngFor = "let i of items | filter : __searchedItem | sortBy : sortByOption">
        <div class="product-card">
          <div class="product-image-wrapper">
            <img class="product-image" [src]="'data:image/jpeg;base64,'+i.pic" alt="{{i.name}}">
            <!-- <div class="product-badge" *ngIf="i.dispPrice && i.dispPrice != i.price">
              <span class="badge-discount">{{calculateDiscount(i.dispPrice, i.price)}}% OFF</span>
            </div> -->
          </div>
          <div class="product-details">
            <h6 class="product-name">{{i.name}}</h6>
            <div class="product-pricing">
              <div class="price-section">
                <span class="current-price">&#x20B9;{{i.price}}</span>
                <del class="original-price" *ngIf="i.dispPrice && i.dispPrice != i.price">&#x20B9;{{i.dispPrice}}</del>
              </div>
            </div>
            <button class="add-to-cart-btn" (click)="addToCart(i.id,1,'')">
              <i class="zmdi zmdi-shopping-cart-plus mr-2"></i>Add to Cart
            </button>
          </div>
      </div>
    </div>
  </div>

  `,
  styles: [`
     .product-grid {
       padding: 15px 0;
     }

     .product-card {
       background: #ffffff;
       border-radius: 15px;
       overflow: hidden;
       box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
       transition: all 0.3s ease;
       height: 100%;
       display: flex;
       flex-direction: column;
       cursor: pointer;
     }

     .product-card:hover {
       transform: translateY(-8px);
       box-shadow: 0 12px 35px rgba(102, 126, 234, 0.25);
     }

     .product-image-wrapper {
       position: relative;
       background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
       padding: 15px;
       display: flex;
       align-items: center;
       justify-content: center;
       height: 150px;
       overflow: hidden;
     }

     .product-image {
       max-width: 100%;
       max-height: 120px;
       width: auto;
       height: auto;
       object-fit: contain;
       transition: transform 0.3s ease;
     }

     .product-card:hover .product-image {
       transform: scale(1.1);
     }

     .product-badge {
       position: absolute;
       top: 10px;
       right: 10px;
       z-index: 10;
     }

     .badge-discount {
       background: linear-gradient(135deg, #FF6B6B 0%, #EE5A6F 100%);
       color: white;
       padding: 6px 12px;
       border-radius: 20px;
       font-size: 0.75rem;
       font-weight: 700;
       box-shadow: 0 3px 10px rgba(255, 107, 107, 0.3);
       letter-spacing: 0.5px;
     }

     .product-details {
       padding: 15px;
       display: flex;
       flex-direction: column;
       flex-grow: 1;
     }

     .product-name {
       font-size: 0.95rem;
       font-weight: 600;
       color: #2d3748;
       margin-bottom: 10px;
       height: 40px;
       overflow: hidden;
       display: -webkit-box;
       -webkit-line-clamp: 2;
       -webkit-box-orient: vertical;
       line-height: 1.4;
     }

     .product-pricing {
       margin-bottom: 12px;
     }

     .price-section {
       display: flex;
       align-items: center;
       justify-content: space-between;
       gap: 10px;
     }

     .current-price {
       font-size: 1.5rem;
       font-weight: 700;
       color: #667eea;
       background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
       -webkit-background-clip: text;
       -webkit-text-fill-color: transparent;
       background-clip: text;
     }

     .original-price {
       font-size: 1rem;
       color: #a0aec0;
       font-weight: 500;
     }

     .add-to-cart-btn {
       width: 100%;
       padding: 10px 16px;
       background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
       color: white;
       border: none;
       border-radius: 10px;
       font-weight: 600;
       font-size: 0.9rem;
       cursor: pointer;
       transition: all 0.3s ease;
       box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
       display: inline-flex;
       align-items: center;
       justify-content: center;
       margin-top: auto;
       white-space: nowrap;
     }

     .add-to-cart-btn:hover {
       background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
       transform: translateY(-2px);
       box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
     }

     .add-to-cart-btn:active {
       transform: translateY(0);
       box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
     }

     .add-to-cart-btn i {
       font-size: 1.2rem;
     }

     /* Responsive adjustments */
     @media (max-width: 768px) {
       .product-card {
         margin-bottom: 20px;
       }
       
       .product-name {
         font-size: 0.95rem;
       }
       
       .current-price {
         font-size: 1.3rem;
       }
     }
  `]
})
export class ProductsListDir {
  constructor(
    public storage: StorageService,
    public cart: CartService
  ) {  }

  @Input("allProductList") items: any = {};
  @Input("searchedText") __searchedItem: string='';
  @Input("sortingBy") sortByOption: string='';
  @Output() refresh:EventEmitter<string> = new EventEmitter();

  ngOnInit() {
    this.sortByOption;
  }
  addToCart(itemId, itemQty, note?: string) {
    this.cart.allItems = this.items;
    this.cart.addToCart(itemId, itemQty, note || '');
    this.refresh.emit('true');
  }

  calculateDiscount(originalPrice: number, currentPrice: number): number {
    if (originalPrice <= currentPrice) return 0;
    return Math.round(((originalPrice - currentPrice) / originalPrice) * 100);
  }
}


