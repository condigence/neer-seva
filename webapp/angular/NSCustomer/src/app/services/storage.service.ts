import { Injectable } from '@angular/core';
@Injectable()

export class StorageService{
  public data: any = {};
  public storageName: string = 'cartinfo';

  constructor() {
    this.loadStorage();
    this.store();
  }

  loadStorage() {
    let temp =  localStorage.getItem(this.storageName);
    if (temp === undefined || temp === null || temp === '') {
      this.data = {};
    } else {
      this.data = JSON.parse( temp );
    }
  }

  set(obj) {

     Object.keys(obj).forEach( (key) => {
       this.data[ key ] = obj[key];
     } );
     this.store();

  }
  store() {
    localStorage.setItem(this.storageName, JSON.stringify(this.data));
    }

  get(key=''){
    if(key!=''){
      const value = this.data[key];
      return value;
    }else{
      return this.data;
    }
  }
  delete(key) {
    delete this.data[ key ];
    this.store();
  }
  deleteAll() {
    this.data = {};
    this.store();
  }

  // public test: any = {};
}
