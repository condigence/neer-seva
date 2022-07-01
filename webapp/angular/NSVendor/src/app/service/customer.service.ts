import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Customer } from '../model/customer.model';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class CustomerService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:3000/api/customers';

  getCustomers() {
    return this.http.get<Customer[]>(this.baseUrl);
  }

  getCustomerById(id: number) {
    return this.http.get<Customer>(this.baseUrl + '/' + id);
  }

  createCustomer(customer: Customer) {
    return this.http.post(this.baseUrl, customer);
  }

  updateCustomer(customer: Customer) {
    return this.http.put(this.baseUrl, customer);
  }

  deleteCustomer(id: number) {
    return this.http.delete(this.baseUrl + '/' + id);
  }
}
