import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

import { BehaviorSubject, Subject, Observable } from "rxjs";

import { Address } from "../model/address.model";
import { environment } from "src/environments/environment";
// import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ "Content-Type": "application/json" }),
};

@Injectable({
  providedIn: "root",
})
export class AddressService {
  constructor(private http: HttpClient) {}

  private baseUrl = environment.apiUrl;

  private currentCartCount = new BehaviorSubject(0);
  currentMessage = this.currentCartCount.asObservable();

  addAddress(address: Address) {
    console.log(address);
    return this.http.post(this.baseUrl + "/v1/address", address);
  }

  getAddressByUserId(id: any): Observable<Address> {
    return this.http.get<Address>(this.baseUrl + "/v1/address/by/user/" + id);
  }

  makeDefaultChange(address: Address) {
    let add = {
      userId: address.userId,
      id: address.id,
    };
    return this.http.put(this.baseUrl + "/v1/makeDefault", add);
  }

  getDefaultAddressByUserId(id: any): Observable<Address> {
    return this.http.get<Address>(
      this.baseUrl + "/v1/getDefault/addresses/by/user/" + id
    );
  }

  getAddressbyId(id: any): Observable<Address> {
    return this.http.get<Address>(this.baseUrl + "addresses/by/" + id);
  }

  updateAddress(address: Address) {
    return this.http.put(
      this.baseUrl + "addresses" + "/by/" + address.id,
      address
    );
  }

  deleteAddress(id: string) {
    return this.http.delete(this.baseUrl + "addresses" + "/by/" + id);
  }

  deleteAddressByUserId(id: string) {
    return this.http.delete(this.baseUrl + "addresses" + "/by/" + id);
  }
}
