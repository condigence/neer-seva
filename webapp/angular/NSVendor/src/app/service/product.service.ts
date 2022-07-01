import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Product } from '../model/product.model';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class ProductService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:3000/api/products';

  getProducts() {
    return this.http.get<Product[]>(this.baseUrl);
  }

  getProductById(id: number) {
    return this.http.get<Product>(this.baseUrl + '/' + id);
  }

  createProduct(product: Product) {
    return this.http.post(this.baseUrl, product);
  }

  updateProduct(product: Product) {
    return this.http.put(this.baseUrl, product);
  }

  deleteProduct(id: number) {
    return this.http.delete(this.baseUrl + '/' + id);
  }
}
