import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { CartService } from '../services/cart.service';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { OrderService } from '../services/order.service';
import { HttpClient } from '@angular/common/http';
import { AddressService } from '../services/address.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'billing-dir',
  template: `
    <form [formGroup]="billingForm" (ngSubmit)="send()" class="billing-form">
      <div class="form-row">
        <div class="form-group col-md-6" *ngFor="let formFields of __billing">
          <label class="form-label">{{formFields.label}}</label>
          
          <div *ngIf="formFields.type=='text'">
            <input
              type="text"
              class="form-control"
              formControlName="{{formFields.uid}}"
              placeholder="{{formFields.placeholder}}"
              [ngClass]="{'is-invalid': billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched}" />
            <div class="invalid-feedback" 
                 *ngIf="billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched">
              <i class="zmdi zmdi-alert-circle-o mr-1"></i>{{formFields.errorMsg}}
            </div>
          </div>

          <div *ngIf="formFields.type=='select'">
            <select class="form-control" formControlName="{{formFields.uid}}"
                    [ngClass]="{'is-invalid': billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched}">
              <option value="">Select {{formFields.label}}</option>
              <option *ngFor="let optionName of formFields.options" value="{{optionName}}">{{optionName}}</option>
            </select>
            <div class="invalid-feedback"
                 *ngIf="billingForm.controls[formFields.uid].status=='INVALID' && billingForm.controls[formFields.uid].touched">
              <i class="zmdi zmdi-alert-circle-o mr-1"></i>{{formFields.errorMsg}}
            </div>
          </div>
        </div>
      </div>

      <div class="form-footer">
        <a routerLink="/products" class="btn-continue-shopping">
          <i class="zmdi zmdi-shopping-cart mr-2"></i>Continue Shopping
        </a>
        <button type="submit" class="btn-place-order" [disabled]="billingForm.invalid || !cart.cartItemsList || !cart.cartTotal">
          <i class="zmdi zmdi-arrow-right mr-2"></i>Proceed to Payment
        </button>
      </div>
    </form>
  `,
  styles: [`
    .billing-form {
      padding: 30px 20px;
    }

    .form-row {
      margin: 0 -10px;
    }

    .form-group {
      padding: 0 10px;
      margin-bottom: 25px;
      transition: all 0.3s ease;
    }

    .form-group:hover {
      transform: translateX(5px);
    }

    .form-label {
      font-weight: 600;
      color: #4a5568;
      font-size: 0.95rem;
      margin-bottom: 8px;
      display: block;
      letter-spacing: 0.3px;
    }

    .form-control {
      border: 2px solid #e2e8f0;
      border-radius: 10px;
      padding: 0px 0px;
      font-size: 0.95rem;
      transition: all 0.3s ease;
      background-color: #f7fafc;
    }

    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
      background-color: #ffffff;
      outline: none;
    }

    .form-control::placeholder {
      color: #a0aec0;
      font-style: italic;
    }

    select.form-control {
      cursor: pointer;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23667eea' d='M10.293 3.293L6 7.586 1.707 3.293A1 1 0 00.293 4.707l5 5a1 1 0 001.414 0l5-5a1 1 0 10-1.414-1.414z'/%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: right 15px center;
      padding-right: 40px;
    }

    .is-invalid {
      border-color: #fc8181 !important;
      background-color: #fff5f5 !important;
      animation: shake 0.3s ease;
    }

    .is-invalid:focus {
      box-shadow: 0 0 0 4px rgba(252, 129, 129, 0.1) !important;
    }

    @keyframes shake {
      0%, 100% { transform: translateX(0); }
      25% { transform: translateX(-5px); }
      75% { transform: translateX(5px); }
    }

    .invalid-feedback {
      display: block;
      color: #e53e3e;
      font-size: 0.875rem;
      margin-top: 8px;
      font-weight: 500;
    }

    .invalid-feedback i {
      font-size: 1rem;
      vertical-align: middle;
    }

    .form-footer {
      display: flex;
      gap: 15px;
      padding-top: 20px;
      border-top: 2px solid #e2e8f0;
      margin-top: 20px;
      flex-wrap: wrap;
    }

    .btn-place-order {
      flex: 1;
      min-width: 250px;
      padding: 15px 30px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      border-radius: 12px;
      color: white;
      font-weight: 600;
      font-size: 1.05rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }

    .btn-place-order:hover:not(:disabled) {
      background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 25px rgba(102, 126, 234, 0.4);
    }

    .btn-place-order:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      transform: none;
    }

    .btn-place-order i {
      font-size: 1.3rem;
    }

    .loading-content {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .spinner-border-sm {
      width: 1.2rem;
      height: 1.2rem;
      border-width: 0.15em;
    }

    .btn-continue-shopping {
      padding: 15px 30px;
      background: linear-gradient(135deg, #56CCF2 0%, #2F80ED 100%);
      border: none;
      border-radius: 12px;
      color: white;
      font-weight: 600;
      font-size: 1.05rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(47, 128, 237, 0.3);
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }

    .btn-continue-shopping:hover {
      background: linear-gradient(135deg, #2F80ED 0%, #56CCF2 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 25px rgba(47, 128, 237, 0.4);
      text-decoration: none;
      color: white;
    }

    .btn-continue-shopping i {
      font-size: 1.3rem;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .billing-form {
        padding: 20px 10px;
      }

      .form-group {
        padding: 0 5px;
      }

      .form-group:hover {
        transform: none;
      }

      .form-footer {
        flex-direction: column;
      }

      .btn-place-order,
      .btn-continue-shopping {
        width: 100%;
      }
    }
  `]
})

export class BillingDir implements OnInit, OnDestroy {
  public billingForm: any = {};
  public errorsInfo: any = {};
  @Input('billingFields') __billing: any = {};

  currentUser: any;
  order: any;
  isPlacingOrder: boolean = false;
  private navigationSubscription: any;

  constructor(
    public fb: FormBuilder,
    public storage: StorageService,
    private authenticationService: AuthenticationService,
    public cart: CartService,
    private orderService: OrderService,
    private http: HttpClient,
    private addressService: AddressService,
    public router: Router,
    private toastr: ToastrService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit() {
    this.loadForm();
  }

  ngOnDestroy() {
    // Clean up subscription if needed
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

    // Always fetch the latest default address when user is logged in
    // This ensures we get the most recent default address
    if (this.currentUser) {
      this.loadLatestDefaultAddress(billingInfo);
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

  loadLatestDefaultAddress(existingBillingInfo: any) {
    try {
      // Fill name/email/mobile from currentUser if available
      const name = this.currentUser.name || '';
      const nameParts = name.split(' ');
      const firstName = nameParts.length > 0 ? nameParts[0] : '';
      const lastName = nameParts.length > 1 ? nameParts.slice(1).join(' ') : '';

      const patch: any = {};
      
      // Only patch user info if not already in form
      if (this.billingForm.controls['firstName'] && !existingBillingInfo.firstName) {
        patch['firstName'] = firstName;
      }
      if (this.billingForm.controls['lastName'] && !existingBillingInfo.lastName) {
        patch['lastName'] = lastName;
      }
      if (this.billingForm.controls['email'] && !existingBillingInfo.email) {
        patch['email'] = this.currentUser.email || '';
      }
      if (this.billingForm.controls['mobile'] && !existingBillingInfo.mobile) {
        patch['mobile'] = this.currentUser.contact || '';
      }

      // Always fetch and update the default address (even if billing info exists)
      const custId = this.currentUser.id;
      if (custId) {
        this.addressService.getDefaultAddressByUserId(custId).subscribe(
          (addr: any) => {
            if (addr) {
              // Always update address fields with latest default address
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
              // Apply the patch values
              this.billingForm.patchValue(patch);
              // Persist into storage so checkout flow sees it
              this.storage.set({ customerInfo: this.billingForm.value });
            } else if (Object.keys(patch).length > 0) {
              // No address found, still apply basic profile patch if available
              this.billingForm.patchValue(patch);
              this.storage.set({ customerInfo: this.billingForm.value });
            }
          },
          (err) => {
            console.error('Error fetching default address:', err);
            // Apply whatever we have from profile
            if (Object.keys(patch).length > 0) {
              this.billingForm.patchValue(patch);
              this.storage.set({ customerInfo: this.billingForm.value });
            }
          }
        );
      } else if (Object.keys(patch).length > 0) {
        // No customer id, just patch with profile info
        this.billingForm.patchValue(patch);
        this.storage.set({ customerInfo: this.billingForm.value });
      }
    } catch (e) {
      console.error('Error while auto-populating billing form', e);
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
    if (this.currentUser) {
      if (this.billingForm.valid) {
        // Store billing information
        this.storage.set({
          customerInfo: this.billingForm.value
        });
        
        // Navigate to payment page
        this.router.navigate(['/payment']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  placeOrder() {
    if (this.currentUser) {
      if (this.billingForm.valid) {
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
          console.log('Placing order with data:', this.order);
          
          // Show loading spinner
          this.isPlacingOrder = true;
          
          try {
            this.orderService.placeMyOrder(this.order)
              .subscribe(
                data => {
                  console.log('Order placed successfully:', data);
                  this.isPlacingOrder = false;
                  this.toastr.success('Order placed successfully! Redirecting...', 'Success');
                  // Navigate to checkout success page
                  setTimeout(() => {
                    this.router.navigate(['/checkout']);
                  }, 500);
                }, 
                error => {
                  console.error('Error placing order:', error);
                  console.error('Error status:', error.status);
                  console.error('Error message:', error.message);
                  
                  // Hide loading spinner
                  this.isPlacingOrder = false;
                  
                  let errorMessage = 'There was an error placing your order. Please try again.';
                  if (error.error && error.error.message) {
                    errorMessage = error.error.message;
                  } else if (error.message) {
                    errorMessage = error.message;
                  }
                  
                  this.toastr.error(errorMessage, 'Order Failed');
                }
              );
          } catch (e) {
            console.error('Exception while placing order:', e);
            this.isPlacingOrder = false;
            this.toastr.error('An unexpected error occurred. Please try again.', 'Error');
          }
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
                  this.toastr.error('Unable to find shop information. Please try again.', 'Error');
                }
              } else {
                console.error('No shops returned from', shopsUrl);
                this.toastr.error('No shops available. Please contact support.', 'Error');
              }
            },
            (err) => {
              console.error('Failed to load shops for default selection', err);
              this.toastr.error('Failed to load shop information. Please try again.', 'Error');
            }
          );
        }
      }
    } else {
      this.router.navigate(['/login']);
    }
  }
}
