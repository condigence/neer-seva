import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../model/user.model';
import {Observable} from 'rxjs/index';




const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class ProfileService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:3000/neerseva/api/v1/profile/';



  // getProfileByUserById(user_id: any): Observable<Profile> {
  //   return this.http.get<Profile>('http://localhost:8090/image/userProfileImage' +  '/' + user_id);
  // }

  
  updateProfileUser(user: User) {
    return this.http.put(this.baseUrl + user.id, user);
  }

 

}
