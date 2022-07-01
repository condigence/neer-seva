import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

import { User } from '../model/user.model';
import { Observable, BehaviorSubject } from 'rxjs/index';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Shop } from '../model/shop.model';

// const httpOptions = {
//   headers: new HttpHeaders({ 'Content-Type': 'application/json' })
// };

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class UserService {
  //  private handleError(error: HttpErrorResponse) {
  //   if (error.error instanceof ErrorEvent) {
  //     // A client-side or network error occurred. Handle it accordingly.
  //     console.error('An error occurred:', error.error.message);
  //   } else {
  //     // The backend returned an unsuccessful response code.
  //     // The response body may contain clues as to what went wrong,
  //     console.error(`Backend returned code ${error.status}, ` + `body was: ${error.error}`);
  //   }
  //   // return an observable with a user-facing error message
  //   return throwError('Something bad happened. Please try again later.');
  // }
  // getAllUsers() {
  //   return this.http.get<User[]>(this.baseUrl + 'users');
  //   //.pipe(catchError(this.handleError));
  // }
  // // getAllUserView() {
  // //   return this.http.get<UserView[]>(this.baseUrl + 'userview');
  // // }
  // getUserById(user_id: any): Observable<User> {
  //   return this.http.get<User>(this.baseUrl + 'users' +  '/' + user_id);
  // }
  // addUser(user: User) {
  //   return this.http.post(this.baseUrl + 'users', user);
  // }
  // updateUser(user: User) {
  //   return this.http.put(this.baseUrl + 'users' +  '/' + user.id, user);
  // }
  // deleteUser(user_id: string) {
  //   return this.http.delete(this.baseUrl + 'users' +  '/' + user_id);
  // }
  // getUserCount() {
  //   return this.http.get(this.baseUrl + 'usercounts');
  // }
  // getUserCustomerTypeCount() {
  //   return this.http.get(this.baseUrl + 'customerscount');
  // }
  // Service for  User type
  // getAllUserTypes() {
  //   return this.http.get<UserType[]>(this.baseUrl + 'usertypes');
  // }
  // getUserTypeById(user_type_id: any): Observable<UserType> {
  //   return this.http.get<UserType>(this.baseUrl + 'usertypes' + '/' + user_type_id);
  // }
  // // getUserView() {
  // //   return this.http.get<UserView[]>(this.baseUrl + 'userview');
  // // }
  // addUserType(usertype: UserType) {
  //   return this.http.post(this.baseUrl + 'usertypes', usertype);
  // }
  // updateUserType(usertype: UserType) {
  //   return this.http.put(this.baseUrl + 'usertypes' +  '/' + usertype.user_type_id, usertype);
  // }
  // deleteUserType(user_type_id: string) {
  //   return this.http.delete(this.baseUrl + 'usertypes' + '/' + user_type_id);
  // }


  constructor(
    private http: HttpClient
  ) { }


  //private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  private baseUrl = environment.USER_API_URL;
  private shopsUrl = environment.STOCK_API_URL;
  

  // private baseUrl = 'http://127.0.0.1:3000/neerseva/api/v1/';

  //  private handleError(error: HttpErrorResponse) {
  //   if (error.error instanceof ErrorEvent) {
  //     // A client-side or network error occurred. Handle it accordingly.
  //     console.error('An error occurred:', error.error.message);
  //   } else {
  //     // The backend returned an unsuccessful response code.
  //     // The response body may contain clues as to what went wrong,
  //     console.error(`Backend returned code ${error.status}, ` + `body was: ${error.error}`);
  //   }
  //   // return an observable with a user-facing error message
  //   return throwError('Something bad happened. Please try again later.');
  // }


  // getAllUsers() {
  //   return this.http.get<User[]>(this.baseUrl + 'users');
  //   //.pipe(catchError(this.handleError));
  // }

  // // getAllUserView() {
  // //   return this.http.get<UserView[]>(this.baseUrl + 'userview');
  // // }


  // getUserById(user_id: any): Observable<User> {
  //   return this.http.get<User>(this.baseUrl + 'users' +  '/' + user_id);
  // }

  // addUser(user: User) {
  //   return this.http.post(this.baseUrl + 'users', user);
  // }

  // updateUser(user: User) {
  //   return this.http.put(this.baseUrl + 'users' +  '/' + user.id, user);
  // }

  // deleteUser(user_id: string) {
  //   return this.http.delete(this.baseUrl + 'users' +  '/' + user_id);
  // }


  // getUserCount() {
  //   return this.http.get(this.baseUrl + 'usercounts');
  // }

  // getUserCustomerTypeCount() {
  //   return this.http.get(this.baseUrl + 'customerscount');
  // }





  // Service for  User type


  // getAllUserTypes() {
  //   return this.http.get<UserType[]>(this.baseUrl + 'usertypes');
  // }

  // getUserTypeById(user_type_id: any): Observable<UserType> {
  //   return this.http.get<UserType>(this.baseUrl + 'usertypes' + '/' + user_type_id);
  // }

  // // getUserView() {
  // //   return this.http.get<UserView[]>(this.baseUrl + 'userview');
  // // }

  // addUserType(usertype: UserType) {
  //   return this.http.post(this.baseUrl + 'usertypes', usertype);
  // }

  // updateUserType(usertype: UserType) {
  //   return this.http.put(this.baseUrl + 'usertypes' +  '/' + usertype.user_type_id, usertype);
  // }

  // deleteUserType(user_type_id: string) {
  //   return this.http.delete(this.baseUrl + 'usertypes' + '/' + user_type_id);
  // }


  getUserById(id: any): Observable<User> {
    // console.log(id);
    return this.http.get<User>(this.baseUrl +'users/' + id);
  }

  loginInUser(user) {
    return this.http.post<any>(this.baseUrl + 'login', user);
  }

  updateUser(user: User) {
    return this.http.put(this.baseUrl + 'users', user);
  }

  getAllOutlets() {
    return this.http.get<Shop[]>(this.shopsUrl + 'stocks/shops');
  }

  // logoutUser() {
  //   localStorage.removeItem('currentUser');
  //   this.loggedIn.next(false);
  //   // this.router.navigate(['']).then(() => {
  //   //   window.location.reload();
  //   // });
  // }

  // get isLoggedIn() {
  //   return this.loggedIn.asObservable(); // {2}
  // }



}
