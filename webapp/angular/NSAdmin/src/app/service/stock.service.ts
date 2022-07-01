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

  private baseUrl = environment.apiUrl;

  getAllStocks() {
    return this.http.get<Stock[]>(this.baseUrl);
  }


  // getStockById(STOCK_ID: any) {
  //   return this.http.get<Stock>(this.baseUrl + '/' + STOCK_ID);
  // }


  // addStock(stock: Stock) {
  //   return this.http.post(this.baseUrl, stock);
  // }

  // updateStock(stock: Stock) {
  //   return this.http.put(this.baseUrl  + '/' + stock.STOCK_ID, stock);
  // }

  deleteStock(id: any) {
    return this.http.delete(this.baseUrl + '/v1/stock/' + id);
  }


}
