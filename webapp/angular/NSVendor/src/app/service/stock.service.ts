import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Stock } from '../model/stock.model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class StockService {

  constructor(private http: HttpClient) {}

  private baseUrl = environment.STOCKS_API_URL;
  private brandsUrl = environment.BRANDS_API_URL;
  private itemsUrl = environment.ITEMS_API_URL;


  addStock(stock: any) {
    return this.http.post(this.baseUrl+'/', stock);
  }

  getAllStocks() {
    return this.http.get<Stock[]>(this.baseUrl + 'stocks');
  }

  getAllstocksWithItem() {
    return this.http.get<Stock[]>(this.baseUrl + 'stockview');
  }

  checkStockByItemId(stock: Stock) {
    return this.http.get<Stock>(this.baseUrl + 'check/items/by/shop');
  }

  getStockById(id: any) {
    return this.http.get<Stock>(this.baseUrl + '/v1/stocks/' + id);
  }


  updateStock(stock: Stock) {
    console.log(stock);
    return this.http.put(this.baseUrl  +'/v1/stock/'+stock.id, stock );
  }

  deleteStock(id: any) {
    return this.http.delete(this.baseUrl + '/v1/stock/' + id);
  }

  getShopByUserId(id) {
    return this.http.get<Stock>(this.baseUrl + '/shops/by/shop/' + id);
  }

  getStockByUserId(id) {
    return this.http.get<Stock>(this.baseUrl + 'items/by/user/' + id);
  }

  getStockByShopId(id) {
    return this.http.get<Stock[]>(this.baseUrl + 'by/shop/' + id);
  }


}
