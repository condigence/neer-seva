import { Component, Input, Output, EventEmitter, OnInit, NgModule } from '@angular/core';
import { ProductsModel } from '../../model/products.model';
import { ItemService } from 'src/app/services/item.service';
import { ItemView } from 'src/app/model/item.view.';
import { UserService } from 'src/app/services/user.service';
import { CartService } from 'src/app/services/cart.service';
import { ToastService } from 'src/app/services/toast.service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  styleUrls: ['./product.pages.scss'],
  template: `
 <menu></menu>
 
<div class="vendor-section-wrapper" style="margin-top: 100px;">
  <app-my-carousel (messageEvent)="receiveMessage($event)" [receivedParentMessage]="messageToSendP"></app-my-carousel>
</div>

 <div class="container">
 <div class="form-row align-items-center">
 <div class="col-md-9">
 <div class="input-group mb-4 mt-2">
 <div class="input-group-prepend">
 <div class="input-group-text">Search Products</div>
 </div>
 <input [(ngModel)]= "searchText" class="form-control" placeholder="Please enter any product name to search ">
 </div>
 </div>
 <div class="col-md-3">
 <div class=" mb-4 mt-2">
 <select class="form-control" [(ngModel)] = "sortOption">
 <option value="name|asc">Sort By Name (A to Z)</option>
 <option value="name|desc">Sort By Name (Z to A)</option>
 <option value="price|lth">Sort By Price (Low to High)</option>
 <option value="price|htl">Sort By Price (High to Low)</option>
 </select>
 </div>
 </div>
 </div>
 <div class="row">
 <div class="col-md-7">
   <!-- Loader -->
   <div *ngIf="isLoading" class="loader-container">
     <div class="loader-spinner">
       <div class="spinner-circle"></div>
       <div class="spinner-circle"></div>
       <div class="spinner-circle"></div>
     </div>
     <p class="loading-text">Loading products...</p>
   </div>

   <!-- Products List -->
   <div *ngIf="!isLoading">
     <productslist-dir
       (refresh)="ref($event)"
       [allProductList]="items"
       [searchedText]="searchText"
       [sortingBy]="sortOption"
     ></productslist-dir>
     
     <!-- No Items Found Message -->
     <div *ngIf="filteredItemsCount === 0 && !isLoading && !shopErrorMessage" class="no-items-container">
       <div class="no-items-content">
         <i class="zmdi zmdi-shopping-basket" style="font-size: 4rem; color: #ccc;"></i>
         <h4>No Products Found</h4>
         <p *ngIf="searchText">No products match your search "<strong>{{searchText}}</strong>"</p>
         <p *ngIf="!searchText">No products available at the moment.</p>
         <button *ngIf="searchText" class="btn btn-primary btn-sm mt-2" (click)="searchText = ''">
           Clear Search
         </button>
       </div>
     </div>
     
     <div *ngIf="shopErrorMessage" class="mt-3 alert alert-warning">
       {{ shopErrorMessage }}
     </div>
   </div>
 </div>
 
 <div class="col-md-5">
 <cart
 *ngIf="cartflag"
 ></cart>
 </div>
 </div>
 </div>

<!-- Login Success Modal -->
<div class="modal fade show" *ngIf="showLoginSuccessModal" style="display: block; background-color: rgba(0,0,0,0.5);">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-body text-center p-4">
        <i class="zmdi zmdi-check-circle zmdi-hc-3x text-success mb-3"></i>
        <h4 class="text-success">Welcome!</h4>
        <p class="mb-0">You are successfully logged in</p>
      </div>
    </div>
  </div>
</div>

<!-- Vendor Switch Confirmation Modal -->
<div class="modal fade show" *ngIf="showVendorSwitchModal" style="display: block; background-color: rgba(0,0,0,0.5);">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header bg-warning text-dark">
        <h5 class="modal-title">
          <i class="zmdi zmdi-alert-triangle mr-2"></i>Switch Vendor?
        </h5>
        <button type="button" class="close" (click)="cancelVendorSwitch()">
          <span>&times;</span>
        </button>
      </div>
      <div class="modal-body p-4">
        <div class="text-center mb-3">
          <i class="zmdi zmdi-shopping-cart zmdi-hc-3x text-warning"></i>
        </div>
        <h5 class="text-center mb-3">Clear Your Cart?</h5>
        <p class="text-center">
          Your cart contains items from another vendor. We currently <strong>do not support multi-vendor orders</strong>.
        </p>
        <p class="text-center text-danger font-weight-bold">
          Switching vendors will remove all items from your cart.
        </p>
        <p class="text-center text-muted small">
          Do you want to clear your cart and continue with the new vendor?
        </p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" (click)="cancelVendorSwitch()">
          <i class="zmdi zmdi-close mr-1"></i>Cancel
        </button>
        <button type="button" class="btn btn-warning text-dark font-weight-bold" (click)="confirmVendorSwitch()">
          <i class="zmdi zmdi-delete mr-1"></i>Clear Cart & Switch
        </button>
      </div>
    </div>
  </div>
</div>

  `
})

export class ProductsPage implements OnInit {
  public cartflag: boolean = false;
  public sortBy: string = '';
  public sortOption: string = 'name|asc';
  items: ItemView[] = [];
  searchText: string = '';
  vendors:any;
  shopErrorMessage: string = '';
  isLoading: boolean = false;
  showLoginSuccessModal: boolean = false;
  showVendorSwitchModal: boolean = false;
  pendingVendorId: any = null;

  // Computed property to get filtered items count
  get filteredItemsCount(): number {
    if (!this.items || this.items.length === 0) {
      return 0;
    }
    if (!this.searchText) {
      return this.items.length;
    }
    // Filter items based on search text - use nested property path
    return this.items.filter(item => {
      const itemName = (item as any)?.item?.name || item.name || '';
      return itemName.toLowerCase().includes(this.searchText.toLowerCase());
    }).length;
  }

/////
message: string;
//vendorId: string;
shopId: string;
messageToSendP: string = '';

////

  constructor(
    public product: ProductsModel,
    public itemService: ItemService,
    public userService: UserService,
    private cartService: CartService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private router: Router
  ) {

  }

  ngOnInit() {
    // Clear vendor selection on page load - force user to select
    localStorage.removeItem('selectedShop');
    
    // Check if user just logged in
    this.route.queryParams.subscribe(params => {
      if (params['justLoggedIn'] === 'true') {
        console.log('justLoggedIn detected, showing modal');
        
        // Clear the query param immediately so it doesn't show on refresh
        this.router.navigate([], {
          queryParams: {},
          replaceUrl: true
        });
        
        // Show success modal
        setTimeout(() => {
          this.showLoginSuccessModal = true;
          // Auto-hide after 2 seconds
          setTimeout(() => {
            this.showLoginSuccessModal = false;
          }, 2000);
        }, 300);
      } else {
        console.log('No justLoggedIn param found');
      }
    });
    
    this.ref();
    this.getDefaultStock();
   // let defaultVendorId = this.getVendorIdByName("NeerSeva");
    //localStorage.setItem('selectedVendor', defaultVendorId);
  
  }

  // getVendorIdByName(vendorname){
  //   //vendorId = 
  //   //return this.vendorId;
  // }
  // getAllVendorItems() {
  //   this.userService.getAllOutlets().subscribe(data => {
  //     this.vendors = data;
  //     console.log(this.vendors);
  //   });
  // }
  ref(event?: any) {
    this.cartflag = false;
    setTimeout(() => {
      this.cartflag = true;
    }, 10);
  }

  getDefaultStock(): void {
    this.isLoading = true;
    this.itemService.getAllItemsWithImage().subscribe(
      (data: any) => {
        console.log('getDefaultStock response', data);
        this.shopErrorMessage = '';
        // API may return different shapes. Try common variants.
        if (Array.isArray(data)) {
          this.items = data as ItemView[];
        } else if (data && data.Items && Array.isArray(data.Items)) {
          this.items = data.Items;
        } else if (data && data.items && Array.isArray(data.items)) {
          this.items = data.items;
        } else if (data && data.data && Array.isArray(data.data)) {
          this.items = data.data;
        } else {
          // fallback
          const vals = data && typeof data === 'object' ? Object.values(data).filter(v => Array.isArray(v))[0] : null;
          if (Array.isArray(vals)) {
            this.items = vals as ItemView[];
          } else {
            console.warn('getDefaultStock: unexpected response shape', data);
            this.items = [];
          }
        }
        // Set items in cart service to refresh cart display
        this.cartService.allItems = this.items;
        this.isLoading = false;
      },
      (err) => {
        console.error('Failed to load default stock', err);
        this.items = [];
        this.isLoading = false;
        // if backend returned a structured error with errorMessage, show friendly message
        try {
          const backendMsg = err && err.error && err.error.errorMessage ? err.error.errorMessage : null;
          if (backendMsg && backendMsg.indexOf('Items Not Found') !== -1) {
            this.shopErrorMessage = 'Empty Stock for this Shop. Please contact Admin.';
          } else if (backendMsg) {
            this.shopErrorMessage = backendMsg;
          } else {
            this.shopErrorMessage = 'Failed to load stock. Please try again later.';
          }
        } catch (e) {
          this.shopErrorMessage = 'Failed to load stock. Please try again later.';
        }
      }
    );
  }

  receiveMessage($event) {
    this.shopId = $event;
    
    // Check if cart has items and user is switching vendors
    const currentVendor = localStorage.getItem('selectedShop');
    const hasCartItems = this.cartService.cartItemsList && this.cartService.cartItemsList.length > 0;
    
    if (currentVendor && currentVendor !== this.shopId && hasCartItems) {
      // Store pending vendor switch and show confirmation modal
      this.pendingVendorId = this.shopId;
      this.showVendorSwitchModal = true;
      return;
    }
    
    // No cart items or same vendor - proceed with switch
    this.switchToVendor(this.shopId);
  }
  
  confirmVendorSwitch() {
    // Clear the cart
    this.cartService.emptyCart();
    
    // Close modal
    this.showVendorSwitchModal = false;
    
    // Proceed with vendor switch
    this.switchToVendor(this.pendingVendorId);
    this.pendingVendorId = null;
    
    // Refresh cart UI
    this.ref();
  }
  
  cancelVendorSwitch() {
    // Close modal without switching
    this.showVendorSwitchModal = false;
    this.pendingVendorId = null;
    // Keep current vendor selection
  }
  
  switchToVendor(vendorId: any) {
   localStorage.setItem('selectedShop', vendorId);
   this.shopId = vendorId;
   this.shopErrorMessage = '';
   this.isLoading = true;
   this.itemService.getStockItemsByShopId(+vendorId).subscribe(
     (data: any) => {
       // clear any previous error
       this.shopErrorMessage = '';
       if (Array.isArray(data)) {
         this.items = data as ItemView[];
       } else if (data && data.items && Array.isArray(data.items)) {
         this.items = data.items;
       } else if (data && data.Items && Array.isArray(data.Items)) {
         this.items = data.Items;
       } else {
         // if API returned empty or unknown, normalize to empty array
         this.items = Array.isArray(data) ? data : [];
       }
       // Set items in cart service to refresh cart display
       this.cartService.allItems = this.items;
       this.isLoading = false;
       console.log(this.items);
     },
     (err) => {
       console.log('Error loading stock for shop', this.shopId, err);
       this.items = [];
       this.isLoading = false;
       // if backend returned structured error, check for Items Not Found
       try {
         const backendMsg = err && err.error && err.error.errorMessage ? err.error.errorMessage : null;
         if (backendMsg && backendMsg.indexOf('Items Not Found') !== -1) {
           this.shopErrorMessage = 'Empty Stock for this Shop. Please contact Admin.';
         } else if (backendMsg) {
           this.shopErrorMessage = backendMsg;
         } else {
           this.shopErrorMessage = 'Failed to load stock for this shop.';
         }
       } catch (e) {
         this.shopErrorMessage = 'Failed to load stock for this shop.';
       }
     }
   );

  }
}
