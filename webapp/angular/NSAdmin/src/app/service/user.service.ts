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

  
  private nodeBaseUrl = 'http://localhost:9092/neerseva/api/v1';

  private usersAPI = environment.USERS_API_URL;


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
