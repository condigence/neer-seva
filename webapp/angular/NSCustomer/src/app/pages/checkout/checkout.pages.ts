import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ProductsModel } from '../../model/products.model';

@Component({
  template:`
   <menu></menu>
  <div class="checkout-wrapper">
    <div class="container">
      <checkout-dir
        [allProductList]="items"
      ></checkout-dir>
    </div>
  </div>
  `,
  styles: [`
    .checkout-wrapper {
      min-height: 100vh;
      padding: 100px 20px 30px;
      background: linear-gradient(135deg, #f5f7fa 0%, #edf2f7 100%);
    }
  `]
})

export class CheckoutPage{
  public cartflag:boolean= false;
  public items: any[] = [];
  constructor(
    public cart: CartService,
    public products: ProductsModel

  ) {

  }

  ngOnInit() {
    this.ref();
  }
  ref() {
    this.cartflag = false;
    setTimeout( () => {
        this.cartflag = true;
    }, 1000);
  }


}
