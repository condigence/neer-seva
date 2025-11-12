import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user.model';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable()
export class UserService {
  constructor(private http: HttpClient) {}

  
  // private nodeBaseUrl = 'http://localhost:9092/neerseva/api/v1/users';

  private usersAPI = environment.USERS_API_URL;
  private vendorsAPI = environment.VENDOR_API_URL;
  private ordersAPI = environment.ORDER_API_URL;

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersAPI);
  }

  getUserById(id: any): Observable<User> {
    return this.http.get<User>(this.usersAPI + id);
  }

  deleteUser(id: string) {
    return this.http.delete(this.usersAPI + id);
  }

  addUser(user: User) {
    return this.http.post(this.usersAPI, user);
  }

  updateUser(user: User) {    
    return this.http.put(this.usersAPI, user);
  }

  register(user: User) {
    return this.http.post(this.usersAPI, user);
  }


  // getAllUsersCount() {
  //   return this.http.get(this.usersAPI + '/active/count');
  // }

  getAllVendorCount() {
    return this.http.get(this.vendorsAPI + 'shops');
  }
    getTop5VendorCount() {
    return this.http.get(this.usersAPI + 'vendors/top/5');
  }

  getTop5CustomerCount() {
    return this.http.get(this.usersAPI + 'customers/top/5');
    
  getAllUsersCount() {
    return this.http.get(this.nodeBaseUrl + '/counts');
  }

  getAllVendorCount() {
    return this.http.get(this.nodeBaseUrl + '/counts');
  }
  
  getAllCustomerCount() {
    return this.http.get(this.nodeBaseUrl + '/counts');
    
  }

  getAllOrderCount() {
    return this.http.get(this.ordersAPI + 'v1/orders');
  }
  

}
