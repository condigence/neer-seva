import { Component, Input } from '@angular/core';
import { CartService } from '../services/cart.service';
import { StorageService } from '../services/storage.service';
import { CompanyDetailsModel } from '../model/companydetails.model';
import { ItemService } from '../services/item.service';
@Component({
    selector: 'checkout-dir',
    template: `



   <div *ngIf="customerDetails && customerDetails.firstName;else emptyCheckout ">
   <!-- Success Message -->
   <div class="success-card d-print-none">
     <div class="success-icon-wrapper">
       <div class="success-checkmark">
         <i class="zmdi zmdi-check"></i>
       </div>
     </div>
     <h2 class="success-title">Order Placed Successfully!</h2>
     <p class="success-message">
       Thank you, <strong>{{customerDetails.firstName}}</strong>! Your order has been confirmed and will be delivered within <strong>1 day</strong>.
     </p>
     <div class="success-actions">
       <button class="btn-print" (click)="print()">
         <i class="zmdi zmdi-print mr-2"></i>Print Invoice
       </button>
       <button class="btn-new-order" (click)="clearCart()">
         <i class="zmdi zmdi-shopping-cart mr-2"></i>Place New Order
       </button>
     </div>
   </div>

    <div class="card">
        <div class="card-header">
            Invoice Date:
            <strong>{{invoiceDate | date:'d-MMM-yyyy'}}</strong>
            <span class="float-right"> <strong>Status:</strong> Pending</span>

        </div>
        <div class="card-body">
        <div class="row">
          <div class="col-md-6 text-left">
          <img src="/assets/images/lg.png" class="logo mr-2 mb-4 float-left"/>
          </div>
          <div class="col-md-6 text-right">
          <h4># INV-{{invoiceNo}}</h4>
          </div>
        </div>
            <div class="row mb-4">
                <div class="col-sm-6">
                    <h6 class="mb-3">From:</h6>
                    <div>
                        <strong>{{companyDetails.name}}</strong>
                    </div>
                    <div>{{companyDetails.address}}</div>
                    <div>{{companyDetails.city}},{{companyDetails.pincode}}</div>
                    <div>Email: {{companyDetails.email}}</div>
                    <div>Phone: {{companyDetails.phone}}</div>
                </div>

                <div class="col-sm-6">
                    <h6 class="mb-3">To:</h6>
                    <div>
                        <strong>{{customerDetails.firstName}} {{customerDetails.lastName}}</strong>
                    </div>
                    <div>{{customerDetails.addressOne}}, {{customerDetails.addressTwo}}</div>
                    <div>{{customerDetails.city}},{{customerDetails.state}},{{customerDetails.zip}}</div>
                    <div>Email: {{customerDetails.email}}</div>
                    <div>Phone: {{customerDetails.mobile}}</div>
                </div>
            </div>

            <div class="table-responsive-sm">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="center">#</th>
                            <th>Item</th>
                            <th class="right">Unit Cost</th>
                            <th class="center">Qty</th>
                            <th class="right">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr *ngFor = "let cartItems of cart.cartItemsList;let i=index;">
                            <td class="center">{{i+1}}</td>
                            <td class="left strong">{{cartItems.name}}</td>
                            <td class="right">{{(cartItems.price/cartItems.qty)}}</td>
                            <td class="center">{{cartItems.qty}}</td>
                            <td class="right">{{cartItems.price}}</td>
                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="row">
                <div class="col-lg-4 col-sm-5">

                </div>

                <div class="col-lg-4 col-sm-5 ml-auto">
                    <table class="table table-clear">
                        <tbody>
                            <tr>
                                <td class="left">
                                    <strong>Subtotal</strong>
                                </td>
                                <td class="right">INR {{cart.cartTotal}}</td>
                            </tr>

                            <tr>
                                <td class="left">
                                    <strong>Total</strong>
                                </td>
                                <td class="right">
                                    <strong>INR {{cart.cartTotal}}</strong>
                                </td>
                            </tr>

                        </tbody>
                    </table>

                </div>

            </div>

        </div>
    </div>
</div>
<ng-template #emptyCheckout>
<div class="card alert alert-primary">
  <div class="card-body">
    Please shop some products & Provide billing information .<a class="btn btn-sm btn-info float-right ml-2" routerLink = '/products'> Continue Shopping</a> &nbsp;  &nbsp; <a class="btn btn-sm btn-info float-right" routerLink = '/billing' *ngIf="cart.cartItemsList && cart.cartTotal"> Go to Billing</a>
  </div>
</div>
   </ng-template>
  `,
    styles: [`
  /* Success Card */
  .success-card {
    background: #ffffff;
    border-radius: 20px;
    padding: 50px 30px;
    text-align: center;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
    margin-bottom: 30px;
    animation: slideDown 0.5s ease-out;
  }

  @keyframes slideDown {
    from {
      opacity: 0;
      transform: translateY(-30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .success-icon-wrapper {
    margin-bottom: 25px;
  }

  .success-checkmark {
    width: 100px;
    height: 100px;
    margin: 0 auto;
    background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    animation: scaleIn 0.5s ease-out 0.2s both;
    box-shadow: 0 10px 30px rgba(17, 153, 142, 0.3);
  }

  @keyframes scaleIn {
    from {
      transform: scale(0);
    }
    to {
      transform: scale(1);
    }
  }

  .success-checkmark i {
    font-size: 4rem;
    color: #ffffff;
    animation: checkPop 0.3s ease-out 0.5s both;
  }

  @keyframes checkPop {
    0%, 100% {
      transform: scale(1);
    }
    50% {
      transform: scale(1.2);
    }
  }

  .success-title {
    font-size: 2rem;
    font-weight: 700;
    color: #2d3748;
    margin-bottom: 15px;
    animation: fadeIn 0.5s ease-out 0.4s both;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  .success-message {
    font-size: 1.2rem;
    color: #718096;
    margin-bottom: 30px;
    line-height: 1.8;
    animation: fadeIn 0.5s ease-out 0.6s both;
  }

  .success-message strong {
    color: #11998e;
    font-weight: 600;
  }

  .success-actions {
    display: flex;
    gap: 15px;
    justify-content: center;
    flex-wrap: wrap;
    animation: fadeIn 0.5s ease-out 0.8s both;
  }

  .btn-print,
  .btn-new-order {
    padding: 15px 30px;
    border: none;
    border-radius: 12px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  }

  .btn-print {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }

  .btn-print:hover {
    background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  }

  .btn-new-order {
    background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
    color: white;
  }

  .btn-new-order:hover {
    background: linear-gradient(135deg, #38ef7d 0%, #11998e 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(17, 153, 142, 0.4);
  }

  .btn-print i,
  .btn-new-order i {
    font-size: 1.2rem;
  }

  /* Invoice Card */
  .card-header {
    padding: .75rem 1.25rem;
    margin-bottom: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-bottom: 1px solid rgba(0,0,0,.125);
    border-radius: 15px 15px 0 0;
  }

  .card {
    border-radius: 15px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    border: none;
  }

  .logo{
    height: 100px;
    width: auto;
  }

  /* Responsive */
  @media (max-width: 768px) {
    .success-card {
      padding: 40px 20px;
    }

    .success-checkmark {
      width: 80px;
      height: 80px;
    }

    .success-checkmark i {
      font-size: 3rem;
    }

    .success-title {
      font-size: 1.5rem;
    }

    .success-message {
      font-size: 1rem;
    }

    .success-actions {
      flex-direction: column;
    }

    .btn-print,
    .btn-new-order {
      width: 100%;
    }
  }

  `]
})

export class CheckOutDir {
    public companyDetails: any = {};
    public customerDetails: any = {};


    public addressDetails: any = {};
    public vendorDetails: any = {};
    public checkOutFlag: any = {};
    public invoiceDate: any = new Date();
    public invoiceNo: any = Math.floor(Math.random() * 10000);

    @Input("allProductList") items: any = {};

    constructor(
        public cart: CartService,
        public storage: StorageService,
        public company: CompanyDetailsModel,
        public itemService: ItemService
    ) {

    }

    ngOnInit() {
        this.customerDetails = this.cart.loadCheckoutInfo('customerInfo');
        this.companyDetails = this.company.companyInfo;
        // If the products list was passed in, use it. Otherwise try to fetch items
        // (prefer stock items for selected shop, fallback to all items).
        const finalize = () => {
            this.cart.allItems = this.items;
            this.cart.listCartItems();
            this.checkOutFlag = JSON.parse(this.storage.get('mycart'));
        };

        if (!this.items || (Array.isArray(this.items) && this.items.length === 0)) {
            const storedShop = localStorage.getItem('selectedShop');
            if (storedShop) {
                this.itemService.getStockItemsByShopId(+storedShop).subscribe(
                    (data: any) => {
                        // normalize response
                        if (Array.isArray(data)) {
                            this.items = data;
                        } else if (data && data.items && Array.isArray(data.items)) {
                            this.items = data.items;
                        } else if (data && data.Items && Array.isArray(data.Items)) {
                            this.items = data.Items;
                        } else {
                            this.items = [];
                        }
                        finalize();
                    },
                    (err) => {
                        console.error('Failed to load stock items for checkout', err);
                        // fallback to all items
                        this.itemService.getAllItemsWithImage().subscribe((all: any) => {
                            if (Array.isArray(all)) {
                                this.items = all;
                            } else if (all && all.items && Array.isArray(all.items)) {
                                this.items = all.items;
                            } else if (all && all.Items && Array.isArray(all.Items)) {
                                this.items = all.Items;
                            } else {
                                this.items = [];
                            }
                            finalize();
                        }, (e) => {
                            console.error('Failed to load all items for checkout fallback', e);
                            this.items = [];
                            finalize();
                        });
                    }
                );
            } else {
                this.itemService.getAllItemsWithImage().subscribe((all: any) => {
                    if (Array.isArray(all)) {
                        this.items = all;
                    } else if (all && all.items && Array.isArray(all.items)) {
                        this.items = all.items;
                    } else if (all && all.Items && Array.isArray(all.Items)) {
                        this.items = all.Items;
                    } else {
                        this.items = [];
                    }
                    finalize();
                }, (e) => {
                    console.error('Failed to load all items for checkout', e);
                    this.items = [];
                    finalize();
                });
            }
        } else {
            finalize();
        }
    }
    clearCart() {
        let temp = {};
        localStorage.setItem(this.storage.storageName, JSON.stringify(temp));
        //this.checkOutFlag = Object.keys(this.storage.get()).length;
        //console.log(this.checkOutFlag)
        document.location.href = '/products';
    }

    print() {
        let temp = {};
        localStorage.setItem(this.storage.storageName, JSON.stringify(temp));
        window.focus();
        window.print();
    }



}
