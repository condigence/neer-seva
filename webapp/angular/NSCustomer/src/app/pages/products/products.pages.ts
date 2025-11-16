import { Component, Input, Output, EventEmitter, OnInit, NgModule } from '@angular/core';
import { ProductsModel } from '../../model/products.model';
import { ItemService } from 'src/app/services/item.service';
import { ItemView } from 'src/app/model/item.view.';
import { UserService } from 'src/app/services/user.service';


@Component({
  styleUrls: ['./product.pages.scss'],
  template: `
 <menu></menu>
 
<div class="container-fluid" style="width: 30%;margin-top: 100px;background: #0AA; border-radius:15px">


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
 <productslist-dir
 (refresh)="ref($event)"
 [allProductList]="items"
 [searchedText]="searchText"
 [sortingBy]="sortOption"
 ></productslist-dir>
        <div *ngIf="shopErrorMessage" class="mt-3 alert alert-warning">
          {{ shopErrorMessage }}
        </div>
 </div>
 
 <div class="col-md-5">
 <cart
 *ngIf="cartflag"
 ></cart>
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

/////
message: string;
//vendorId: string;
shopId: string;
messageToSendP: string = '';

////

  constructor(
    public product: ProductsModel,
    public itemService: ItemService,
    public userService: UserService
  ) {

  }

  ngOnInit() {
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
    this.itemService.getAllItemsWithImage().subscribe(
      (data: any) => {
        this.shopErrorMessage = '';
        // API may return different shapes. Try common variants.
        if (Array.isArray(data)) {
          this.items = data as ItemView[];
          return;
        }

        if (data && data.Items && Array.isArray(data.Items)) {
          this.items = data.Items;
          return;
        }

        if (data && data.items && Array.isArray(data.items)) {
          this.items = data.items;
          return;
        }

        if (data && data.data && Array.isArray(data.data)) {
          this.items = data.data;
          return;
        }

        // fallback
        const vals = data && typeof data === 'object' ? Object.values(data).filter(v => Array.isArray(v))[0] : null;
        if (Array.isArray(vals)) {
          this.items = vals as ItemView[];
          return;
        }

        console.warn('getDefaultStock: unexpected response shape', data);
        this.items = [];
      },
      (err) => {
        console.error('Failed to load default stock', err);
        this.items = [];
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
   localStorage.setItem('selectedShop', this.shopId);
   this.shopErrorMessage = '';
   this.itemService.getStockItemsByShopId(+this.shopId).subscribe(
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
       console.log(this.items);
     },
     (err) => {
       console.log('Error loading stock for shop', this.shopId, err);
       this.items = [];
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
