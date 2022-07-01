import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { delay, map, catchError } from "rxjs/operators";
import { throwError } from "rxjs";
import { User } from "src/app/model/user.model";
import { environment } from "src/environments/environment";
const URL = environment.USERS_API_URL;

@Injectable({
  providedIn: "root",
})
export class MyValidationService {
  constructor(private http: HttpClient) {}

  isContactTaken(user: string) {
    console.log(user);
    return this.http.get<User[]>(URL).pipe(
      delay(1000),
      map((data: User[]) => data.filter((d) => d.contact == +user)),
      map((data: User[]) => data.length > 0),
      catchError((err) => {
        console.log("error", err);
        return throwError(err);
      })
    );
  }
}
