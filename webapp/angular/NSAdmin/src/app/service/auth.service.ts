import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { User } from '../model/user.model';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  private baseUrl = environment.apiUrl;
  private users_api_Url = environment.USERS_API_URL;

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }


  verifyOTP(user: User) {

    return this.http.post<any>(this.baseUrl + `/v1/verify/otp`, user)
      .pipe(map(user => {
        // login successful if there's a jwt token in the response
        // if (user && user.token) {

        if (user) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));

          this.currentUserSubject.next(user);
        }
        return user;
      },
      error => {
        console.log(error);
      }));
  }


  verifyLogin(user: User) {
    return this.http.post<any>(this.baseUrl + `/v1/verify/login`, user)
      .pipe(map(user => {
       // console.log(user);
        if (user) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
         // console.log(JSON.stringify(user.contact));
          localStorage.setItem('userContact', user.contact);

        }
        return user;
      },
      error => {
        console.log(error);
      }));
  }


  // Optional. we can call save directly
  verifyRegistration(user: User) {
    return this.http.post<any>(this.baseUrl + `/v1/verify/registration`, user)
      .pipe(map(data => {

        if (data) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
         // console.log("setting into localstorage "+JSON.stringify(data));
          localStorage.setItem('userContact', data.contact);
          localStorage.setItem('newUser', JSON.stringify(data));
        }
        return data;
      },
      error => {
        console.log(error);
      }));
  }

  logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('userContact');
    localStorage.removeItem('newUser');
    this.currentUserSubject.next(null);

  }


  register(user: User) {
    return this.http.post(this.users_api_Url, user);
  }


}
