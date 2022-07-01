import { Component, Input } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { OrderService } from '../services/order.service';

@Component({
  selector: 'billing-dir',
  template: `

      <form [formGroup]="billingForm" (ngSubmit) = "send()">

      <div class="form-row">

          <div class="form-group col-md-6" *ngFor="let formFields of __billing">

            <label for="inputEmail4">{{formFields.label}}</label>
            <div *ngIf="formFields.type=='text'">
            <input
            type="text"
            class="form-control"
            formControlName="{{formFields.uid}}"
            placeholder="{{formFields.placeholder}}" />

            <small
            class="form-text text-danger"
            *ngIf="billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched"
            >{{formFields.errorMsg}}</small></div>

            <div *ngIf="formFields.type=='select'">
              <select id="inputState" class="form-control" formControlName = "{{formFields.uid}}">
                <option>Select</option>
                <option *ngFor="let optionName of formFields.options" value="{{optionName}}">{{optionName}}</option>
              </select>
              <small class="form-text text-danger"
            *ngIf="billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched"
            >{{formFields.errorMsg}}</small>
            </div>
          </div>

      </div>
    <button type="submit" class="btn btn-sm btn-primary" [disabled]="billingForm.invalid" *ngIf="cart.cartItemsList && cart.cartTotal">Confirm and Place Order</button>
    <a routerLink="/products" class="btn btn-sm btn-info float-right">Continue Shopping</a>

</form>
  `,
  styles: [`
  form .ng-invalid.ng-touched{
    border-color: #dc3545;
  }
  `]
})

export class BillingDir {
  public billingForm: any = {};
  public errorsInfo: any = {};
  @Input('billingFields') __billing: any = {};

  currentUser: any;
  order: any;

  constructor(
    public fb: FormBuilder,
    public storage: StorageService,
    private authenticationService: AuthenticationService,
    public cart: CartService,
    private orderService: OrderService,
    public router: Router
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit() {
    this.loadForm();
  }

  loadForm() {
    let temp = {};
    let billingInfo = this.cart.loadCheckoutInfo('customerInfo');

    if (billingInfo === undefined || billingInfo === '' || billingInfo === null) {
      billingInfo = {};
    }
    (this.__billing).forEach((item) => {

      temp[item.uid] = [billingInfo[item.uid], item.validation];

    });
    
    this.billingForm = this.fb.group(temp);
  }

  clearCart() {
    let temp = {};
    localStorage.setItem(this.storage.storageName, JSON.stringify(temp));
    //this.checkOutFlag = Object.keys(this.storage.get()).length;
    //console.log(this.checkOutFlag)
    //document.location.href = '/products';
}


  send() {

 // alert('send');
    if (this.currentUser) {
    //  console.log(this.currentUser);
      if (this.billingForm.valid) {

        console.log(this.billingForm.value);
       
        this.storage.set({
          customerInfo: this.billingForm.value
        });

        let currentUser = localStorage.getItem('currentUser');
        let custId = JSON.parse(currentUser).id;

        //Get current customer
        let customer = { "customerId": custId };
        //Get Selected vendor
        let shopId = localStorage.getItem('selectedShop');
        let shop = { "id": shopId };

        let items = [];
        let i;

        //Prepare items
        for (i = 0; i < this.cart.cartItemsList.length; i++) {
          let item = { "id": this.cart.cartItemsList[i].id, "quantity": this.cart.cartItemsList[i].qty }
          items.push(item);
        }

        // prepare Order data
        this.order = { "customer": customer, "shop": shop, "items": items };
        this.orderService.placeMyOrder(this.order)
          .subscribe(data => {
            console.log(data);
            this.router.navigate(['/checkout']);
          });
          // Clear cart ofter placing order
          this.clearCart();
      }
    } else {
      this.router.navigate(['/login']);
    }
  }
}
