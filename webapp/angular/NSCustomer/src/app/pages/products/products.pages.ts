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
  vendors:any;

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
  ref() {
    this.cartflag = false;
    setTimeout(() => {
      this.cartflag = true;
    }, 10);
  }

  getDefaultStock(): void {
    this.itemService.getAllItemsWithImage().subscribe(data => {
      this.items = data;
    });
  }

  receiveMessage($event) {
    this.shopId = $event;
   localStorage.setItem('selectedShop', this.shopId);
   this.itemService.getStockItemsByShopId(+this.shopId).subscribe(data => {
    this.items = data;
    console.log(this.items);
  },
  error => {
    console.log(error);
   // this.items = [];
  });

  }
}
