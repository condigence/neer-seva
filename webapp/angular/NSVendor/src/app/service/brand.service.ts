import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {Observable} from 'rxjs/index';
import { Image } from '../model/image.model';
import { Brand } from '../model/brand.model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class BrandService {

  constructor(private http: HttpClient) {}
 
  private baseUrl = environment.BRANDS_API_URL;

  getAllBrands() {
    return this.http.get<Brand[]>(this.baseUrl);
  }

  getBrandById(id: any): Observable<Brand> {
    return this.http.get<Brand>(this.baseUrl + '/v1/brands/' + id);
  }

  deleteBrand(id: string) {
    return this.http.delete(this.baseUrl + '/v1/brands/' + id);
  }

  addBrand(brand: Brand) {
    return this.http.post(this.baseUrl + '/v1/brands', brand);
  }

  updateBrand(brand: Brand) {
    return this.http.put(this.baseUrl + '/v1/brands', brand);
  }


}
