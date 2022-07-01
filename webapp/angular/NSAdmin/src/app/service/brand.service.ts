import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Brand } from '../model/brand.model';
import { Observable } from 'rxjs/index';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class BrandService {

  constructor(private http: HttpClient) { }

  private baseUrl = environment.apiUrl;
  private brandsURL = environment.BRANDS_API_URL;

  getAllBrands() {
    return this.http.get<Brand[]>(this.brandsURL);
  }

  deleteBrand(id: string) {
    return this.http.delete(this.brandsURL + id);
  }

  addBrand(brand: Brand) {
    return this.http.post(this.brandsURL, brand);
  }

  getBrandById(id: any): Observable<Brand> {
    return this.http.get<Brand>(this.brandsURL + id);
  }

  updateBrand(brand: Brand) {
    return this.http.put(this.brandsURL, brand);
  }

}
