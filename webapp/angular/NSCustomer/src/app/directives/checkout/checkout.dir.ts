import { Component, Input } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { StorageService } from '../../services/storage.service';
import { CompanyDetailsModel } from '../../model/companydetails.model';
import { ItemService } from '../../services/item.service';
@Component({
    selector: 'checkout-dir',
    templateUrl: './checkout.dir.html',
    styleUrls: ['./checkout.dir.scss']
})




export class CheckOutDir {
    public companyDetails: any = {};
    public customerDetails: any = {};


    public addressDetails: any = {};
    public vendorDetails: any = {};
    public checkOutFlag: any = {};
    public invoiceDate: any = new Date();
    public invoiceNo: any = Math.floor(Math.random() * 10000);

    @Input("allProductList") items: any = {};

    constructor(
        public cart: CartService,
        public storage: StorageService,
        public company: CompanyDetailsModel,
        public itemService: ItemService
    ) {

    }

    ngOnInit() {
        this.customerDetails = this.cart.loadCheckoutInfo('customerInfo');
        this.companyDetails = this.company.companyInfo;
        // If the products list was passed in, use it. Otherwise try to fetch items
        // (prefer stock items for selected shop, fallback to all items).
        const finalize = () => {
            this.cart.allItems = this.items;
            this.cart.listCartItems();
            this.checkOutFlag = JSON.parse(this.storage.get('mycart'));
        };

        if (!this.items || (Array.isArray(this.items) && this.items.length === 0)) {
            const storedShop = localStorage.getItem('selectedShop');
            if (storedShop) {
                this.itemService.getStockItemsByShopId(+storedShop).subscribe(
                    (data: any) => {
                        // normalize response
                        if (Array.isArray(data)) {
                            this.items = data;
                        } else if (data && data.items && Array.isArray(data.items)) {
                            this.items = data.items;
                        } else if (data && data.Items && Array.isArray(data.Items)) {
                            this.items = data.Items;
                        } else {
                            this.items = [];
                        }
                        finalize();
                    },
                    (err) => {
                        console.error('Failed to load stock items for checkout', err);
                        // fallback to all items
                        this.itemService.getAllItemsWithImage().subscribe((all: any) => {
                            if (Array.isArray(all)) {
                                this.items = all;
                            } else if (all && all.items && Array.isArray(all.items)) {
                                this.items = all.items;
                            } else if (all && all.Items && Array.isArray(all.Items)) {
                                this.items = all.Items;
                            } else {
                                this.items = [];
                            }
                            finalize();
                        }, (e) => {
                            console.error('Failed to load all items for checkout fallback', e);
                            this.items = [];
                            finalize();
                        });
                    }
                );
            } else {
                this.itemService.getAllItemsWithImage().subscribe((all: any) => {
                    if (Array.isArray(all)) {
                        this.items = all;
                    } else if (all && all.items && Array.isArray(all.items)) {
                        this.items = all.items;
                    } else if (all && all.Items && Array.isArray(all.Items)) {
                        this.items = all.Items;
                    } else {
                        this.items = [];
                    }
                    finalize();
                }, (e) => {
                    console.error('Failed to load all items for checkout', e);
                    this.items = [];
                    finalize();
                });
            }
        } else {
            finalize();
        }
    }
    clearCart() {
        let temp = {};
        localStorage.setItem(this.storage.storageName, JSON.stringify(temp));
        //this.checkOutFlag = Object.keys(this.storage.get()).length;
        //console.log(this.checkOutFlag)
        document.location.href = '/products';
    }

    print() {
        let temp = {};
        localStorage.setItem(this.storage.storageName, JSON.stringify(temp));
        window.focus();
        window.print();
    }



}