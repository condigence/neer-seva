import { Injectable, Input } from '@angular/core';
import { StorageService } from './storage.service';
import { AddressService } from './address.service';
@Injectable()

export class CartService {
  private _allItems: any = [];
  public cartData: any = [];
  // public cartItemsList: any  = {};
  public cartItemsList: any = [];
  public cartTotal: any = 0;
  public cartItemsStorageName = 'mycart';

  address;

  // Getter and setter for allItems to trigger cart refresh
  get allItems(): any {
    return this._allItems;
  }
  
  set allItems(value: any) {
    this._allItems = value;
    // Refresh cart items when allItems is set
    if (value && value.length > 0) {
      this.listCartItems();
    }
  }

  constructor(
    private addressService: AddressService,
    public storage: StorageService
  ) {
    this.loadCart();
  }

  loadCart() {
    let temp = this.storage.get('mycart');
    if (temp === undefined || temp === '' || temp === null) {
      this.cartData = {};
    } else {
      this.cartData = temp;
    }
  }

  addToCart(id, qty, replace) {

    if (this.cartData[id] === undefined) {
      this.cartData[id] = 0;
    }
    if (replace === '') {
      this.cartData[id] = this.cartData[id] + qty;
    } else {
      this.cartData[id] = parseInt(qty);
    }
    if (this.cartData[id] === 0) {
      delete this.cartData[id];
    }
    this.storeItems();
  }

  storeItems() {
    this.storage.set({
      'mycart': this.cartData
    });
    this.listCartItems();
  }


  listCartItems() {
    let tempCart = [];
    let getActualItems = Object.keys(this.cartData);
    let cartDataItems = this.cartData;

    let tempTotal = 0;
    var onlyChoosenItems = (this.allItems).filter(function (item) {
      if (getActualItems.indexOf('' + item.id) !== -1) {


        tempCart.push({
          id: item.id,
          name: item.name,
          qty: cartDataItems[item.id],
          mrp: item.mrp,
          price: item.price * cartDataItems[item.id],
        });
        tempTotal += item.price * cartDataItems[item.id];
      }
    });


    this.cartItemsList = tempCart;
    this.cartTotal = tempTotal;
  }


  loadCheckoutInfo(storageKey: string) {
    // Load Default Address and Populate customerInfo
    let currentUser = localStorage.getItem('currentUser');
    let id = JSON.parse(currentUser).id;
    let customerInfo = {}
    this.addressService.getDefaultAddressByUserId(+id)
      .subscribe(Response => {
        this.address = Response;

        customerInfo = {
          firstName: JSON.parse(currentUser).name,
          lastName: " ",
          email: JSON.parse(currentUser).email,
          mobile: JSON.parse(currentUser).contact,
          addressOne: this.address.line1,
          // addressTwo: this.address.line2,
          // city: this.address.city,
          // state: this.address.state,
          // zip: this.address.pin,
          paymentmode: "UPI"

        }
        this.storage.set({
          customerInfo: customerInfo
        });
      });
    return this.storage.get(storageKey);
  }

  emptyCart() {
    this.cartData = {};
    this.cartItemsList = [];
    this.cartTotal = 0;
    this.storage.set({
      mycart: {}
    });
  }
}
