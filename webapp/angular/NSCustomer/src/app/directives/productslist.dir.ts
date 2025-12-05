import { Component, Input, Output, EventEmitter } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../services/user.service';



@Component({
  selector: 'productslist-dir',
  template: `
  
  <div class="row product-grid" >
    <div class="col-lg-4 col-md-6 col-sm-6 mb-4" *ngFor = "let i of items | filter : __searchedItem | sortBy : sortByOption">
        <div class="product-card">
          <div class="product-image-wrapper">
            <img class="product-image" [src]="'data:image/jpeg;base64,'+i.item.pic" alt="{{i.item.name}}">
            <!-- <div class="product-badge" *ngIf="i.item.dispPrice && i.item.dispPrice != i.item.price">
              <span class="badge-discount">{{calculateDiscount(i.item.dispPrice, i.item.price)}}% OFF</span>
            </div> -->
          </div>
          <div class="product-details">
            <h6 class="product-name">{{i.item.name}}</h6>
            <div class="seller-info" *ngIf="getVendorName(i)">
              <i class="zmdi zmdi-store-24 mr-1"></i>
              <span class="seller-label">Seller:</span>
              <span class="seller-name">{{getVendorName(i)}}</span>
            </div>
            <div class="product-pricing">
              <div class="price-section">
                <span class="current-price">&#x20B9;{{i.item.price}}</span>
                <del class="original-price" *ngIf="i.item.dispPrice && i.item.dispPrice != i.item.price">&#x20B9;{{i.item.dispPrice}}</del>
              </div>
            </div>
            
            <!-- Stock Info -->
            <div class="stock-info" *ngIf="i.quantity > 0 && i.quantity <= 10" [class.low-stock-alert]="i.quantity <= 10">
              <i class="zmdi zmdi-alert-circle"></i>
              <span>Only {{i.quantity}} left!</span>
            </div>
            
            <div class="stock-info available-stock" *ngIf="i.quantity > 10">
              <i class="zmdi zmdi-check-circle"></i>
              <span>Available stock: {{i.quantity}}</span>
            </div>
            
            <button class="add-to-cart-btn" 
                    (click)="addToCart(i.id,1,'')" 
                    [disabled]="!i.quantity || i.quantity === 0"
                    [class.out-of-stock-btn]="!i.quantity || i.quantity === 0">
              <i class="zmdi zmdi-shopping-cart-plus mr-2" *ngIf="i.quantity > 0"></i>
              <i class="zmdi zmdi-block mr-2" *ngIf="!i.quantity || i.quantity === 0"></i>
              {{i.quantity > 0 ? 'Add to Cart' : 'Out of Stock'}}
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

     .stock-info {
       border-radius: 8px;
       padding: 8px 12px;
       margin-bottom: 10px;
       display: flex;
       align-items: center;
       gap: 6px;
     }

     .stock-info.low-stock-alert {
       background: #fff5f5;
       border: 1px solid #feb2b2;
       animation: pulse 2s ease-in-out infinite;
     }

     .stock-info.low-stock-alert i {
       color: #e53e3e;
       font-size: 1.1rem;
     }

     .stock-info.low-stock-alert span {
       color: #c53030;
       font-weight: 600;
       font-size: 0.85rem;
     }

     .stock-info.available-stock {
       background: #f0fdf4;
       border: 1px solid #86efac;
     }

     .stock-info.available-stock i {
       color: #16a34a;
       font-size: 1.1rem;
     }

     .stock-info.available-stock span {
       color: #15803d;
       font-weight: 600;
       font-size: 0.85rem;
     }

     @keyframes pulse {
       0%, 100% { opacity: 1; }
       50% { opacity: 0.7; }
     }
     .product-name {
       font-size: 0.95rem;
       font-weight: 600;
       color: #2d3748;
       margin-bottom: 8px;
       height: 40px;
       overflow: hidden;
       display: -webkit-box;
       -webkit-line-clamp: 2;
       -webkit-box-orient: vertical;
       line-height: 1.4;
     }

     .seller-info {
       display: flex;
       align-items: center;
       gap: 4px;
       margin-bottom: 10px;
       padding: 6px 10px;
       background: linear-gradient(135deg, #f0f4ff 0%, #fef3f7 100%);
       border-radius: 8px;
       border: 1px solid rgba(102, 126, 234, 0.15);
     }

     .seller-info i {
       color: #667eea;
       font-size: 1rem;
     }

     .seller-label {
       font-size: 0.8rem;
       color: #718096;
       font-weight: 500;
     }

     .seller-name {
       font-size: 0.85rem;
       color: #667eea;
       font-weight: 600;
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

     .add-to-cart-btn:disabled,
     .out-of-stock-btn {
       background: linear-gradient(135deg, #cbd5e0 0%, #a0aec0 100%);
       cursor: not-allowed;
       opacity: 0.7;
       box-shadow: none;
     }

     .add-to-cart-btn:disabled:hover,
     .out-of-stock-btn:hover {
       transform: none;
       box-shadow: none;
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
  private vendors: any[] = [];
  
  constructor(
    public storage: StorageService,
    public cart: CartService,
    private toastr: ToastrService,
    private userService: UserService
  ) {  }

  @Input("allProductList") items: any = {};
  @Input("searchedText") __searchedItem: string='';
  @Input("sortingBy") sortByOption: string='';
  @Output() refresh:EventEmitter<string> = new EventEmitter();

  ngOnInit() {
    this.sortByOption;
    this.loadVendors();
  }
  
  loadVendors() {
    this.userService.getAllOutlets().subscribe(
      (data: any) => {
        this.vendors = data || [];
      },
      (err) => {
        console.error('Failed to load vendors', err);
        this.vendors = [];
      }
    );
  }
  
  getVendorName(item: any): string {
    if (!item || this.vendors.length === 0) {
      return '';
    }
    
    // Try to get shopId from different possible locations in the item structure
    const shopId = item.shopId || item.shop_id || (item.stock && item.stock.shopId) || null;
    
    if (!shopId) {
      return '';
    }
    
    const vendor = this.vendors.find(v => String(v.id) === String(shopId));
    return vendor ? vendor.name : '';
  }
  addToCart(itemId, itemQty, note?: string) {
    // Check if vendor/shop is selected
    const selectedShop = localStorage.getItem('selectedShop');
    
    if (!selectedShop || selectedShop === 'null' || selectedShop === 'undefined') {
      this.toastr.error(
        'Please select a vendor from the carousel above before adding items to cart.',
        'Vendor Not Selected',
        { 
          timeOut: 5000, 
          progressBar: true,
          closeButton: true,
          positionClass: 'toast-top-center'
        }
      );
      return;
    }
    
    this.cart.allItems = this.items;
    const result = this.cart.addToCart(itemId, itemQty, note || '');
    
    // Only refresh if quantity actually changed
    if (result && result.success) {
      // Check if we hit the stock limit
      if (result.limitReached && result.quantity > 0) {
        const item = this.items.find((i: any) => i.id == itemId);
        const itemName = item?.item?.name || 'This item';
        const availableStock = item?.quantity || 0;
        
        this.toastr.warning(
          `${itemName} has only ${availableStock} items in stock. Cannot add more.`,
          'Limited Stock',
          { timeOut: 4000, progressBar: true }
        );
      }
      
      // Only emit refresh if quantity actually increased
      if (result.quantityChanged) {
        this.refresh.emit('true');
      }
    }
  }

  calculateDiscount(originalPrice: number, currentPrice: number): number {
    if (originalPrice <= currentPrice) return 0;
    return Math.round(((originalPrice - currentPrice) / originalPrice) * 100);
  }
}


