import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Address } from '../model/address.model';



const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:3000/neerseva/';

  getAllAddress() {
    return this.http.get<Address[]>(this.baseUrl + 'addresses');
  }


  getAddressById(address_id: any) {
    return this.http.get<Address>(this.baseUrl + 'addresses' + '/by' + '/'   + address_id);
  }


  addAddress(address: Address) {
    return this.http.post(this.baseUrl + 'addresses', address);
  }

  updateAddress(address: Address) {
    return this.http.put(this.baseUrl + 'addresses' + '/by' + '/' + 'address_id' , address);
  }

  deleteAddress(user_id: string) {
    return this.http.delete(this.baseUrl + 'addresses' + '/by' + '/'  + user_id);
  }


}

