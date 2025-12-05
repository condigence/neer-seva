import { Injectable, Input } from '@angular/core';
import { StorageService } from './storage.service';
import { AddressService } from './address.service';
@Injectable()

export class CartService {
  private _allItems: any = [];
  public cartData: any = {};
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
      // Save snapshot after recalculating cart with product data
      if (this.cartItemsList && this.cartItemsList.length > 0) {
        try {
          this.storage.set({
            cartItems: { stockItems: this.cartItemsList, total: this.cartTotal }
          });
        } catch (e) {
          console.error('allItems setter - error saving snapshot:', e);
        }
      }
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
    // Try to restore a previously stored cart items snapshot so UI shows immediately
    // even if product catalog (allItems) is not yet loaded.
    try {
      const snapshot: any = this.storage.get('cartItems');
      if (snapshot && snapshot.stockItems && snapshot.stockItems.length > 0) {
        this.cartItemsList = snapshot.stockItems;
        this.cartTotal = snapshot.total || 0;
      } else {
        console.log('loadCart - no valid snapshot to restore');
      }
    } catch (e) {
      console.error('loadCart - error restoring snapshot:', e);
    }
  }

  addToCart(id, qty, replace) {
    // Get available stock for this item
    const availableStock = this.getAvailableStock(id);
    
    // Store the previous quantity to detect changes
    const previousQty = this.cartData[id] || 0;
    
    if (this.cartData[id] === undefined) {
      this.cartData[id] = 0;
    }
    
    let requestedQty = 0;
    
    if (replace === '') {
      // relative change (delta)
      let newQty = (this.cartData[id] || 0) + (Number(qty) || 0);
      requestedQty = newQty;
      // Enforce stock limit
      if (availableStock !== null && newQty > availableStock) {
        newQty = availableStock;
      }
      this.cartData[id] = newQty;
    } else {
      // absolute replace
      let next = parseInt(qty, 10);
      if (isNaN(next) || next < 1) {
        next = 1;
      }
      requestedQty = next;
      // Enforce stock limit
      if (availableStock !== null && next > availableStock) {
        next = availableStock;
      }
      this.cartData[id] = next;
    }
    
    const finalQty = this.cartData[id];
    const quantityChanged = finalQty !== previousQty;
    
    if (this.cartData[id] <= 0) {
      delete this.cartData[id];
    }
    
    // Only store and recalculate if quantity actually changed
    if (quantityChanged) {
      this.storeItems();
    }
    
    // Return the actual quantity added (for feedback)
    return {
      success: true,
      quantity: finalQty,
      previousQuantity: previousQty,
      quantityChanged: quantityChanged,
      limitReached: availableStock !== null && finalQty >= availableStock && requestedQty > finalQty
    };
  }
  
  getAvailableStock(id: any): number | null {
    if (!this.allItems || this.allItems.length === 0) {
      return null;
    }
    const item = this.allItems.find((i: any) => i.id == id);
    return item ? (item.quantity ?? null) : null;
  }

  storeItems() {
    this.storage.set({
      'mycart': this.cartData
    });
    // Recompute the full cart view from available product data
    this.listCartItems();
    // Persist a lightweight snapshot of the cart (id/name/qty/price and total)
    // so the UI can show cart contents immediately after a browser refresh
    // Only save snapshot if cartItemsList has actual items (i.e., allItems was loaded)
    try {
      if (this.cartItemsList && this.cartItemsList.length > 0) {
        this.storage.set({
          cartItems: { stockItems: this.cartItemsList, total: this.cartTotal }
        });
      } else {
        console.log('storeItems - skipping snapshot save, cartItemsList is empty (allItems not loaded yet)');
      }
    } catch (e) {
      console.error('storeItems - error saving snapshot:', e);
    }
  }


  listCartItems() {
    console.log('listCartItems called - allItems:', this.allItems?.length, 'cartData keys:', Object.keys(this.cartData).length, 'current cartItemsList:', this.cartItemsList.length);
    
    // Don't clear cart display if allItems hasn't loaded yet and we have cart data
    if (!this.allItems || this.allItems.length === 0) {
      if (Object.keys(this.cartData).length > 0) {
        // Cart has items but product catalog hasn't loaded yet
        // Keep existing cartItemsList from snapshot if available
        return;
      } else {
        this.cartItemsList = [];
        this.cartTotal = 0;
        return;
      }
    }

    const tempCart: any[] = [];
    const getActualItems = Object.keys(this.cartData);
    const cartDataItems = this.cartData;

    let tempTotal = 0;
    (this.allItems as any[]).forEach((catalogItem: any) => {
      // Expect top-level id (used in addToCart), and nested product data under `item`
      const key = '' + catalogItem.id;
      if (getActualItems.indexOf(key) === -1) return;

      const qty = Number(cartDataItems[catalogItem.id]) || 0;
      const product = catalogItem.item ? catalogItem.item : catalogItem;
      const unitPrice = Number(product.dispPrice) || 0;
      const lineTotal = unitPrice * qty;

      tempCart.push({
        id: catalogItem.id,
        itemId: product.id,
        name: product.name || catalogItem.name,
        qty: qty,
        mrp: product.mrp || product.itemMrp || product.dispPrice || product.itemDispPrice || 0,
        price: lineTotal,
        unitPrice: unitPrice,
        image: product.pic || product.image || null,
        availableStock: catalogItem.quantity ?? 0
      });
      tempTotal += lineTotal;
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
      mycart: {},
      cartItems: { stockItems: [], total: 0 }
    });
  }

  clearCart() {
    // Complete cart cleanup - removes all cart data
    this.cartData = {};
    this.cartItemsList = [];
    this.cartTotal = 0;
    this.storage.deleteAll();
  }
}
