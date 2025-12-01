import { Component } from '@angular/core';
import { BillingFormModel } from '../../model/billingformfields.model';

@Component({
  template:`
  <menu></menu>
  <div class="content-wrapper">
    <div class="container-fluid px-0">
      <div class="row no-gutters">
        <div class="col-lg-8 order-md-1 mb-4">
          <div class="card billing-card shadow-lg border-0">
            <div class="card-header bg-gradient text-white">
              <h5 class="mb-0">
                <i class="zmdi zmdi-receipt mr-2"></i>Billing Information
              </h5>
            </div>
            <div class="card-body">
              <billing-dir
                [billingFields]="billing?.data"
              ></billing-dir>
            </div>
          </div>
        </div>
        
        <div class="col-lg-4 order-md-2 mb-4">
          <billing-cart *ngIf="cartflag"></billing-cart>
        </div>
      </div>
    </div>
  </div>
  `,
  styles : [`
    .content-wrapper {
      min-height: 100vh;
      padding: 100px 0 30px;
      background: #f5f7fa;
      width: 100%;
      margin: 0;
    }

    .container-fluid {
      padding: 0 15px;
    }

    .row {
      margin: 0;
    }

    [class*="col-"] {
      padding-left: 10px;
      padding-right: 10px;
    }

    .billing-card {
      border-radius: 15px;
      overflow: hidden;
      animation: slideIn 0.5s ease-out;
    }

    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .bg-gradient {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
      padding: 20px 25px;
    }

    .bg-gradient h5 {
      font-weight: 700;
      font-size: 1.5rem;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
      display: flex;
      align-items: center;
    }

    .bg-gradient i {
      font-size: 1.5rem;
    }

    /* Responsive Styles */
    @media (max-width: 991px) {
      .content-wrapper {
        padding: 80px 0 25px;
      }

      .container-fluid {
        padding: 0 12px;
      }

      [class*="col-"] {
        padding-left: 8px;
        padding-right: 8px;
      }

      .bg-gradient {
        padding: 15px 20px;
      }

      .bg-gradient h5 {
        font-size: 1.3rem;
      }
    }

    @media (max-width: 768px) {
      .content-wrapper {
        padding: 75px 0 20px;
      }

      .container-fluid {
        padding: 0 10px;
      }

      [class*="col-"] {
        padding-left: 5px;
        padding-right: 5px;
      }

      .col-lg-8,
      .col-lg-4 {
        max-width: 100%;
        flex: 0 0 100%;
      }

      .bg-gradient {
        padding: 12px 15px;
      }

      .bg-gradient h5 {
        font-size: 1.2rem;
      }

      .bg-gradient i {
        font-size: 1.3rem;
      }
    }

    @media (max-width: 480px) {
      .content-wrapper {
        padding: 70px 0 15px;
      }

      .container-fluid {
        padding: 0 8px;
      }

      [class*="col-"] {
        padding-left: 4px;
        padding-right: 4px;
      }

      .bg-gradient {
        padding: 10px 12px;
      }

      .bg-gradient h5 {
        font-size: 1.1rem;
      }

      .bg-gradient i {
        font-size: 1.2rem;
      }

      .mb-4 {
        margin-bottom: 1rem !important;
      }
    }
  `]
})

export class BillingPage{
public cartflag:boolean= false;
  constructor(
    public billing:BillingFormModel
  ){
   // console.log(billing);
  }

  ngOnInit(){
    this.ref();
  }
  ref(){
    this.cartflag = false;
    setTimeout( () => {
        this.cartflag = true;
    }, 100 )
  }

}
