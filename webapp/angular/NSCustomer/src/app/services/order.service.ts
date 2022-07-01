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

  private baseUrl = environment.ORDER_API_URL;

  getOrderByUserById(userId: any): Observable<any> {
    return this.http.get<any>(this.baseUrl +  'orders/byCustomer/' + userId);
  }

  placeMyOrder(order: any) {
    return this.http.post(this.baseUrl+'orders/', order);
  }

  


}
