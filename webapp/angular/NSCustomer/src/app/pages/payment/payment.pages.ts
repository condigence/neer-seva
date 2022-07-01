import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ProductsModel } from '../../model/products.model';

@Component({
  template:`
  <div class="container register">
			<div class="row">
				
				<div class="col-md-4">
					<div class="tab-content" id="myTabContent">
						<div class="tab-pane  show active" id="home" role="tabpanel">
							<h1 class="register-heading">Pay With Paytm</h1>
							<div class="row register-form">
								<div class="col-md-10">
									<div class="form-group">
										<input id="ORDER_ID" tabindex="1" maxlength="20" size="20"
											   name="ORDER_ID" autocomplete="off" class="form-control" placeholder="ORDER ID" >
									</div>
									<div class="form-group">
										<input type="text"  placeholder="Enter customer ID" value=""
											   name="CUST_ID"/>
									</div>
									<div class="form-group">
										<input type="text"  placeholder="Enter Industry type id" value=""
											   name="INDUSTRY_TYPE_ID"/>
									</div>
									<div class="form-group">
										<input type="text"  placeholder="Enter Channel" value=""
											   name="CHANNEL_ID"/>
									</div>
									<div class="form-group">
										<input type="text"  placeholder="Enter Amount" value=""
											   name="TXN_AMOUNT"/>
									</div>
									<button type="submit" class="btnRegister" style="align : center">Pay Now</button>
								</div>
							</div>
						</div>
					</div>
	
				</div>
			</div>
		</div>
`
})

export class PaymentPage{
  public cartflag:boolean= false;
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
