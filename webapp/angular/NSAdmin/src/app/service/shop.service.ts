import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Shop } from '../model/shop.model';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:3000/neerseva/shops';

  getAllShops() {
    return this.http.get<Shop[]>(this.baseUrl);
  }

  getShopById(SHOP_ID: any) {
    return this.http.get<Shop>(this.baseUrl + '/' + SHOP_ID);
  }

  addShop(shop: Shop) {
    return this.http.post(this.baseUrl, shop);
  }

  updateShop(shop: Shop) {
    return this.http.put(this.baseUrl  + '/' + shop.SHOP_ID, shop);
  }

  deleteShop(SHOP_ID: string) {
    return this.http.delete(this.baseUrl + '/' + SHOP_ID);
  }


}
