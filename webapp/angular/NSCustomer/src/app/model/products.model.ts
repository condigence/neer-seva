import { Injectable } from '@angular/core';
import { Validators } from '@angular/forms';

@Injectable()
export class ProductsModel{
  public data =
    {
      p_id : '1',
      product_name : 'Surf Excel',
      product_weight : '1Kg',
      product_price : '590',
      product_image : 'https://rukminim1.flixcart.com/image/832/832/jg6v24w0/washing-powder/3/r/d/2-2-kg-top-load-surf-excel-original-imaf3udkm4v5mdrq.jpeg?q=70'
    };

}
