import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StockService } from '../service/stock.service';
import { Stock } from '../model/stock.model';
import { AuthenticationService } from '../service/auth.service';
import { ProfileService } from '../service/profile.service';
import { ItemService } from '../service/item.service';
import { Item } from '../model/item.model';
import { ShopService } from '../service/shop.service';
import { UserService } from '../service/user.service';


@Component({
  selector: 'app-list-stock',
  templateUrl: './list-stock.component.html',
  styleUrls: ['./list-stock.component.scss']
})
export class ListStockComponent implements OnInit {
  public popoverTitle: string = 'Are You Sure to Delete??';
  public popoverMessage: string = 'You will no longer with this record';
  public confirmClicked: boolean = false;
  public cancelClicked: boolean = false;


  // stocks: Stock[];
  // name;
  // stockuser;
  currentUser: any;
  user: any;
  shops: any;
  stocks: any;
  result: any;
  // name: any;

  constructor(
    private router: Router,
    private stockService: StockService,
    private authenticationService: AuthenticationService,
    private shopService: ShopService    
  ) { }


  ngOnInit() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    // localStorage.getItem('currentUser');
    this.getShopByUserId(this.currentUser.id);
  }

  getShopByUserId(id) {
    this.shopService.getShopByVendorId(id)
      .subscribe(response => {
        this.shops = response;
        
      });
  }

  onChangeShop(id) {
    if (id) {
      this.stockService.getStockByShopId(id)
        .subscribe(
          data => { 
            if(data.length === 0)  {
              this.result = 'NO item';
              console.log(this.result);
            }        
            this.stocks = data; 
            // console.log( "line number 67, list-stock",this.stocks);                      
          });
    }    
    else {
      this.stocks = 'No Records';
    }
  }

  

  deleteStock(stock) {
    console.log(stock);
    this.stockService.deleteStock(+stock.id)
      // .subscribe(data => {
      //   this.getShopByUserId(this.currentUser.id);
      // });
       // Update UI locally
    this.stocks = this.stocks.filter(s => s.id !== stock.id);
  }

  updateStock(stocks: Stock) {
    console.log(stocks);
    localStorage.removeItem('editStockId');
    localStorage.setItem('editStockId', String(stocks.id));
    this.router.navigate(['edit-stock']);
  }



  // getAllStocksWithItem() {
  //   this.stockService
  //   .getAllstocksWithItem()
  //   .subscribe(data => {
  //     this.stocks = data;
  //   });
  // }

  // getStockByShopId(id) {
  //         this.stockService
  //         .getStockByUserId(id)
  //         .subscribe(data => {
  //         //  this.stockuser = data;
  //         });
  // }





  // Search() {
  //   if (this.stocks !==  '') {
  //   } else if (this.name === '') {
  //     this.ngOnInit();
  //   }
  //   this.stocks = this.stocks.filter(res => {
  //     return res.name.toLowerCase().match(this.name.toLowerCase());
  //   });
  // }






}
