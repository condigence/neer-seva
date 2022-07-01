import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../model/user.model';
import {Observable} from 'rxjs/index';
import { environment } from 'src/environments/environment';
//import { Order } from '../model/order.model';

const httpOptions = {
  
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable()
export class OrderService {


  constructor(private http: HttpClient) {}

  private baseUrl = environment.ORDERS_API_URL;

  getOrderByUserById(id: any): Observable<any> {
    return this.http.get<any>(this.baseUrl +  'to/vendor/' + id);
  }

  deliverOrder(order: any) {
    return this.http.post(this.baseUrl+'place', order);
  }

  getNumberOfOrdersByVendorId(id: any) {
    return this.http.get(this.baseUrl + 'vendor/ordercount/' + id);
  }


}
