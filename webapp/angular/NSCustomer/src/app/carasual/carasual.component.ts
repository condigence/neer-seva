
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ProductsModel } from '../model/products.model';
import { ItemService } from '../services/item.service';
import { UserService } from '../services/user.service';
import { ItemView } from '../model/item.view.';
import { Shop } from '../model/shop.model';

@Component({
  selector: 'app-my-carousel',
  templateUrl: './carasual.component.html',
  styleUrls: ['./carasual.component.scss']
})
export class CarasualComponent implements OnInit {

   outlets: any;
 // outlets:Shop = [];
  outletId: number;
  imageModuleName: string;

  constructor(
    public userService: UserService
  ) {

  }

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();

  ngOnInit() {
    this.getAllOutlets();
  }
  getAllOutlets() {
    this.userService.getAllOutlets().subscribe(data => {
      this.outlets = data;    
    });
  }

  getShopItemsByShopId(shop: any): void {
    this.messageEvent.emit(shop.id);
  }


}
