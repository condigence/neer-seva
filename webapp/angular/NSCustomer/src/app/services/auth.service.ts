import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";

import { User } from "../model/user.model";
import { environment } from "src/environments/environment";

const httpOptions = {
  headers: new HttpHeaders({ "Content-Type": "application/json" }),
};

@Injectable({ providedIn: "root" })
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  private baseUrl = environment.USER_API_URL;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem("currentUser"))
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  verifyLogin(user: User) {
    return this.http.post<any>(this.baseUrl + `verify/login`, user).pipe(
      map(
        (user) => {
          // console.log(user);
          if (user) {
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            // console.log(JSON.stringify(user.contact));
            localStorage.setItem("userContact", user.contact);
            // localStorage.setItem('currentUser', JSON.stringify(user));
            //  this.currentUserSubject.next(user);
          }
          return user;
        },
        (error) => {
          console.log(error);
        }
      )
    );
  }

  verifyOTP(user: User) {
    return this.http.post<any>(this.baseUrl + `verify/otp`, user).pipe(
      map(
        (user) => {
          // login successful if there's a jwt token in the response
          //if (user && user.token) {

          if (user) {
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            localStorage.setItem("currentUser", JSON.stringify(user));
            this.currentUserSubject.next(user);
          }
          return user;
        },
        (error) => {
          console.log(error);
        }
      )
    );
  }

  // Optional. we can call save directly
  verifyRegistration(user: User) {
    //console.log(user);
    return this.http
      .post<any>(this.baseUrl + `verify/registration`, user)
      .pipe(
        map(
          (data) => {
            //console.log(data);
            if (data) {
              // store user details and jwt token in local storage to keep user logged in between page refreshes
              // console.log("setting into localstorage "+JSON.stringify(data));
              localStorage.setItem("userContact", data.contact);
              localStorage.setItem("newUser", JSON.stringify(data));
            }
            return data;
          },
          (error) => {
            console.log(error);
          }
        )
      );
  }

  logout() {
    console.log("Logged Out!");
    localStorage.removeItem("currentUser");
    localStorage.removeItem("userContact");
    this.currentUserSubject.next(null);
    localStorage.setItem("userloggedOut", "true");
    console.log(localStorage.getItem("currentuser"));
  }
}
