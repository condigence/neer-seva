import { Component, Input, Output, EventEmitter, OnInit, NgModule } from '@angular/core';
import { ProductsModel } from '../../model/products.model';
import { ItemService } from 'src/app/services/item.service';
import { ItemView } from 'src/app/model/item.view.';
import { UserService } from 'src/app/services/user.service';
import { CartService } from 'src/app/services/cart.service';
import { ToastService } from 'src/app/services/toast.service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-products',
  templateUrl: './products.page.html',
  styleUrls: ['./product.pages.scss'] 
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
        
        // After login, if vendor was selected, load that vendor's items
        const selectedShop = localStorage.getItem('selectedShop');
        if (selectedShop) {
          this.switchToVendor(selectedShop);
        } else {
          this.getDefaultStock();
        }
      } else {
        console.log('No justLoggedIn param found');
        
        // Clear vendor selection only on fresh page load (not after login)
        localStorage.removeItem('selectedShop');
        this.getDefaultStock();
      }
    });
    
    this.ref();
  
  }
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
