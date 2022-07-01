import { Injectable } from '@angular/core';

@Injectable()
export class CompanyDetailsModel{

  public companyInfo : any =
    {
      name : 'Condigence',
      address : 'Ma21,SMN Road',
      city: 'Ara',
      pincode: '802301',
      email: 'customer.care@condigence.com',
      phone : '080-43232123'
    }


}
