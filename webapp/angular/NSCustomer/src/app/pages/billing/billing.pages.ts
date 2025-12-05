import { Component } from '@angular/core';
import { BillingFormModel } from '../../model/billingformfields.model';

@Component({
selector: 'app-billing',
templateUrl: './billing.pages.html',
styleUrls: ['./billing.pages.scss'] 
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
