import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Address } from '../model/address.model';
import { environment } from 'src/environments/environment';



const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private http: HttpClient) {}

  private addAuthAPI = environment.AUTH_API_URL;

  getAllAddress() {
    return this.http.get<Address[]>(this.addAuthAPI + 'addresses');
  }


  getAddressById(addressId: any) {
    return this.http.get<Address>(this.addAuthAPI + 'v1/addresses/by/'   + addressId);
  }


  addAddress(address: Address) {
    return this.http.post(this.addAuthAPI + 'addresses', address);
  }

  updateAddress(address: Address) {
    return this.http.put(this.addAuthAPI + 'addresses' + '/by' + '/' + 'address_id' , address);
  }

  deleteAddress(user_id: string) {
    return this.http.delete(this.addAuthAPI + 'addresses' + '/by' + '/'  + user_id);
  }


}

