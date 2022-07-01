import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Shop } from '../model/shop.model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http: HttpClient) {}


  private baseUrl = environment.STOCKS_API_URL;
  
  getAllShops(id) {
    return this.http.get<Shop[]>(this.baseUrl + 'shops/' + id);
  }

  getShopByVendorId(id: any) {
    return this.http.get<Shop[]>(this.baseUrl + 'shops/vendor/' + id);
  }

  getShopById(id: any) {
    return this.http.get<Shop>(this.baseUrl + 'shops/' + id);
  }

  deleteShop(id: number) {
    return this.http.delete(this.baseUrl + 'shops/' + id);
  }

  addShop(shop: Shop) {
    return this.http.post(this.baseUrl + 'shops', shop);
  }

  updateShop(shop: Shop) {
    return this.http.put(this.baseUrl  + 'shops', shop);
  }

  


}
