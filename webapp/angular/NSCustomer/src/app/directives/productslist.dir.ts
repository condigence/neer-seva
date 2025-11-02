import { Component, Input, Output, EventEmitter } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';



@Component({
  selector: 'productslist-dir',
  template: `
  
  <div class="row" >
    <div class="col-md-4" *ngFor = "let i of items | filter : __searchedItem | sortBy : sortByOption">
        <div class="card">
        <div>
          <img class="rounded mx-auto d-block img-responsive mt-3" [src]="'data:image/jpeg;base64,'+i.pic" style="width:100%" alt="Card image cap">
          </div>
          <div class="card-body">
          <p class="card-title">{{i.name}}</p>
            <div class="row">
              <div class="col-md-6">
                <del class="card-text"> <small>&#x20B9;{{i.dispPrice}}</small></del>
              </div>
              <div class="col-md-6">
              <p class="text-right"> <small>&#x20B9;{{i.price}}</small> </p>
              </div>
            </div>
            <div class="row" style="margin-left: 20%;">
            <button class="btn btn-sm btn-secondary" (click)="addToCart(i.id,1,'')">Add to Cart</button></div>
          </div>
      </div>
    </div>

  `,
  styles: [`
     input{ margin: 5px; }
     img{
       height:120px;
       width:auto
     }
     .col-md-4{
       margin-bottom:20px;
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
}


