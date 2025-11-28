import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable()
export class OrderService {
  constructor(private http: HttpClient) {}

  private ordersAPI = environment.ORDER_API_URL;

  getAllOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.ordersAPI);
  }

  getOrderById(id: any): Observable<any> {
    return this.http.get<any>(this.ordersAPI + id);
  }

  deleteOrder(id: string) {
    return this.http.delete(this.ordersAPI + id);
  }

  createOrder(order: any): Observable<any> {
    return this.http.post<any>(this.ordersAPI, JSON.stringify(order), httpOptions);
  }

  updateOrder(order: any): Observable<any> {
    return this.http.put<any>(this.ordersAPI + order.id, JSON.stringify(order), httpOptions);
  }

    getAllOrderCount() {
    return this.http.get(this.ordersAPI + 'count');
  }
}
