// src/app/services/interceptor.service.ts
import { Injectable } from '@angular/core';
import {
    HttpInterceptor, HttpRequest,
    HttpHandler, HttpEvent, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
    providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
    constructor() { }
    handleError(error: HttpErrorResponse) {
        // Better logging for debugging
        if (error.error instanceof ErrorEvent) {
            // Client-side / network error
            console.error('Client-side error:', error.error.message);
        } else {
            // Server-side error
            console.error(`Server returned code ${error.status}, body was:`, error.error);
        }
        // retain original HttpErrorResponse for downstream handlers
        console.log('Network Error! Please contact Admin!');
        return throwError(() => error);
    }
    intercept(req: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
        return next.handle(req)
            .pipe(
                catchError(this.handleError)
            );
    }
}
