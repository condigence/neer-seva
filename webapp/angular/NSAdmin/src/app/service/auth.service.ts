import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

import { User } from '../model/user.model';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private http: HttpClient) {
    const raw = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User>(raw ? JSON.parse(raw) : null);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  // Prefer an explicit auth URL if provided in environment; fall back to generic apiUrl
  private baseUrl = environment.AUTH_API_URL || environment.apiUrl;
  private users_api_Url = environment.USERS_API_URL;

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }


  verifyOTP(user: User) {
    return this.http.post<any>(`${this.baseUrl}/v1/verify/otp`, user)
      .pipe(
        map(u => {
          if (u) {
            localStorage.setItem('currentUser', JSON.stringify(u));
            this.currentUserSubject.next(u);
          }
          return u;
        }),
        catchError(err => {
          console.error('verifyOTP error', err);
          return throwError(() => err);
        })
      );
  }


  verifyLogin(user: User) {
    const url = `${this.baseUrl}/v1/verify/login`;
    console.debug('verifyLogin POST URL:', url, 'payload:', user);
    return this.http.post<any>(url, user)
      .pipe(
        map(u => {
          if (u) {
            localStorage.setItem('userContact', u.contact);
          }
          return u;
        }),
        catchError(err => {
          // Add more context when logging to help diagnose 404s from environment/path issues
          console.error('verifyLogin error at', url, err);
          return throwError(() => err);
        })
      );
  }


  // Optional. we can call save directly
  verifyRegistration(user: User) {
    return this.http.post<any>(`${this.baseUrl}/v1/verify/registration`, user)
      .pipe(
        map(data => {
          if (data) {
            localStorage.setItem('userContact', data.contact);
            localStorage.setItem('newUser', JSON.stringify(data));
          }
          return data;
        }),
        catchError(err => {
          console.error('verifyRegistration error', err);
          return throwError(() => err);
        })
      );
  }

  logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('userContact');
    localStorage.removeItem('newUser');
    this.currentUserSubject.next(null);

  }


  /**
   * Register a new user. Returns the full HttpResponse so callers can inspect status codes (e.g. 201 Created).
   */
  register(user: User): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.users_api_Url, user, { observe: 'response' as 'body' });
  }


}
