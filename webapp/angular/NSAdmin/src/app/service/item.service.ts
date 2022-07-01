import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Item } from '../model/item.model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private http: HttpClient) { }

  private itemUrl = environment.ITEMS_API_URL;

  getAllItems() {
    return this.http.get<Item[]>(this.itemUrl);
  }

  getItemById(id: any) {
    return this.http.get<Item>(this.itemUrl+ id);
  }

  deleteItem(id: string) {
    return this.http.delete(this.itemUrl+ id);
  }

  addItem(item: Item) {
    return this.http.post(this.itemUrl, item);
  }

  updateItem(item: Item) {
    return this.http.put(this.itemUrl, item);
  }

}
