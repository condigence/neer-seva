import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Item } from '../model/item.model';
import { Brand } from '../model/brand.model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private http: HttpClient) {}

  private baseUrl = environment.ITEMS_API_URL;

  getItemsByBrandId(id: string)  {
    //console.log(id);
    return this.http.get<Item>(this.baseUrl + 'by/brands/'+ id);
  }

  

}
