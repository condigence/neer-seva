import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../model/user.model';
import {Observable} from 'rxjs/index';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class UserService {

  constructor(private http: HttpClient) {}


  private baseUrl = environment.apiUrl;
  private nodeBaseUrl = 'http://localhost:3000/neerseva/api/v1';

  getAllUsers() {
    return this.http.get<User[]>(this.baseUrl + '/v1/users');
  }

  getUserById(id: any): Observable<User> {
    return this.http.get<User>(this.baseUrl + '/v1/users/' + id);
  }

  deleteUser(id: string) {
    return this.http.delete(this.baseUrl + '/v1/users/'+ id);
  }

  addUser(user: User) {
    return this.http.post(this.baseUrl + '/v1/users', user);
  }

  updateUser(user: User) {
    return this.http.put(this.baseUrl + '/v1/user', user);
  }

  register(user: User) {
    return this.http.post(this.baseUrl + '/v1/users', user);
  }

  getAllUsersCount() {
    return this.http.get(this.nodeBaseUrl + '/usercounts');
  }

  getAllVendorCount() {
    return this.http.get(this.nodeBaseUrl + '/vendorscount');
  }
  
  getAllCustomerCount() {
    return this.http.get(this.nodeBaseUrl + '/customerscount');
  }

  getAllOrderCount() {
    return this.http.get(this.nodeBaseUrl + '/ordercount');
  }


}
