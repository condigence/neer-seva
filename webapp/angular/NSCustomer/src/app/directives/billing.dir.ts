import { Component, Input } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { OrderService } from '../services/order.service';
import { HttpClient } from '@angular/common/http';
import { AddressService } from '../services/address.service';

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
    private http: HttpClient,
    private addressService: AddressService,
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

    // If billing info is not present but user is logged in, try to auto-populate
    // from the user's default address and profile info.
    if ((!billingInfo || Object.keys(billingInfo).length === 0) && this.currentUser) {
      try {
        // Fill name/email/mobile from currentUser if available
        const name = this.currentUser.name || '';
        const nameParts = name.split(' ');
        const firstName = nameParts.length > 0 ? nameParts[0] : '';
        const lastName = nameParts.length > 1 ? nameParts.slice(1).join(' ') : '';

        const patch: any = {};
        if (this.billingForm.controls['firstName']) {
          patch['firstName'] = firstName;
        }
        if (this.billingForm.controls['lastName']) {
          patch['lastName'] = lastName;
        }
        if (this.billingForm.controls['email']) {
          patch['email'] = this.currentUser.email || '';
        }
        if (this.billingForm.controls['mobile']) {
          patch['mobile'] = this.currentUser.contact || '';
        }

        // Attempt to fetch default address and merge into patch
        const custId = this.currentUser.id;
        if (custId) {
          this.addressService.getDefaultAddressByUserId(custId).subscribe(
            (addr: any) => {
              if (addr) {
                if (this.billingForm.controls['addressOne']) {
                  patch['addressOne'] = addr.line1 || addr.lineOne || '';
                }
                if (this.billingForm.controls['addressTwo']) {
                  patch['addressTwo'] = addr.line2 || addr.lineTwo || '';
                }
                if (this.billingForm.controls['city']) {
                  patch['city'] = addr.city || '';
                }
                if (this.billingForm.controls['state']) {
                  patch['state'] = addr.state || '';
                }
                if (this.billingForm.controls['zip']) {
                  patch['zip'] = addr.pin || addr.zip || '';
                }
                // apply the patch values
                this.billingForm.patchValue(patch);
                // persist into storage so checkout flow sees it
                this.storage.set({ customerInfo: this.billingForm.value });
              } else {
                // no address found, still apply basic profile patch
                this.billingForm.patchValue(patch);
                this.storage.set({ customerInfo: this.billingForm.value });
              }
            },
            (err) => {
              console.error('Failed to load default address for user', custId, err);
              // apply whatever we have from profile
              this.billingForm.patchValue(patch);
              this.storage.set({ customerInfo: this.billingForm.value });
            }
          );
        } else {
          // no customer id, just patch with profile info
          this.billingForm.patchValue(patch);
          this.storage.set({ customerInfo: this.billingForm.value });
        }
      } catch (e) {
        console.error('Error while auto-populating billing form', e);
      }
    }
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

        // Get current customer
        let customer = { "customerId": custId };

        const prepareAndPlace = (shopIdValue: any) => {
          const shop = { id: Number(shopIdValue) };

          let items = [];
          for (let i = 0; i < this.cart.cartItemsList.length; i++) {
            let item = { id: this.cart.cartItemsList[i].id, quantity: this.cart.cartItemsList[i].qty };
            items.push(item);
          }

          // prepare Order data
          this.order = { customer: customer, shop: shop, items: items };
          this.orderService.placeMyOrder(this.order)
            .subscribe(data => {
              console.log(data);
              this.router.navigate(['/checkout']);
            });
          // Clear cart after placing order
          this.clearCart();
        };

        // Get Selected vendor from localStorage; if not present, fetch shops and pick first
        let storedShop = localStorage.getItem('selectedShop');
        if (storedShop && storedShop !== 'undefined' && storedShop !== 'null') {
          prepareAndPlace(storedShop);
        } else {
          // fetch shops and pick the first one
          const shopsUrl = 'http://localhost:9093/neerseva/api/v1/stocks/shops';
          this.http.get<any[]>(shopsUrl).subscribe(
            (shops) => {
              if (shops && shops.length > 0) {
                const first = shops[0];
                const firstId = first.id ?? first.shopId ?? first.shop_id ?? first._id ?? null;
                if (firstId !== null && firstId !== undefined) {
                  localStorage.setItem('selectedShop', String(firstId));
                  prepareAndPlace(firstId);
                } else {
                  console.error('No id found on first shop item', first);
                }
              } else {
                console.error('No shops returned from', shopsUrl);
              }
            },
            (err) => {
              console.error('Failed to load shops for default selection', err);
            }
          );
        }
      }
    } else {
      this.router.navigate(['/login']);
    }
  }
}
