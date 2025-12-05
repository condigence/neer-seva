
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { StorageService } from '../../services/storage.service';
import { CartService } from '../../services/cart.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/user.service';



@Component({
  selector: 'productslist-dir',
    templateUrl: './productlist.dir.html',
    styleUrls: ['./productlist.dir.scss']
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
          positionClass: 'toast-top-right'
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