import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ProductsModel } from '../../model/products.model';

@Component({
selector: 'app-checkout',
templateUrl: './checkout.pages.html',
styleUrls: ['./checkout.pages.scss']
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
