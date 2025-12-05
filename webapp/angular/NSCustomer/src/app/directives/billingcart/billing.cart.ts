import { Component } from "@angular/core";
import { CartService } from "../../services/cart.service";
@Component({
  selector: "billing-cart",
  templateUrl: "./billing.cart.html",
  styleUrls: ["./billing.cart.scss"],
})
export class BillingCartDir {
  constructor(public cart: CartService) {}

  ngOnInit() {
    // Force cart to reload from localStorage when component initializes
    this.cart.loadCart();
  }
}
