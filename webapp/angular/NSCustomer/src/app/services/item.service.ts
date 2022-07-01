import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Item } from '../model/item.model';
import { BehaviorSubject, Subject } from 'rxjs';
import { Brand } from '../model/brand.model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(
    private http: HttpClient,

  ) { }

  private baseUrl = environment.CATALOG_API_URL;
  private stockUrl = environment.STOCK_API_URL;

  private currentCartCount = new BehaviorSubject(0);
  currentMessage = this.currentCartCount.asObservable();


  getAllItemsWithImage() {
    return this.http.get<Item[]>(this.baseUrl+'items');
  }

  getItemById(id: any) {
    return this.http.get<Item>(this.baseUrl + 'items' + '/' + id);
  }

  getItemsByVendorId(id: any) {
   // return this.http.get<Item[]>(this.baseUrl );
  }

  getStockItemsByShopId(id: any) {
    
   return this.http.get<Item[]>(this.stockUrl + 'stocks/items/by/shop/' + id);
  }


}
