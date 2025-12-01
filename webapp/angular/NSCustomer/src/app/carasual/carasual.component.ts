
import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
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

  @ViewChild('sliderContainer') sliderContainer: ElementRef;
  outlets: any;
  // outlets:Shop = [];
  outletId: number;
  imageModuleName: string;
  currentSlide: number = 0;

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

  onScroll(): void {
    if (this.sliderContainer) {
      const container = this.sliderContainer.nativeElement;
      const scrollLeft = container.scrollLeft;
      const cardWidth = container.offsetWidth;
      this.currentSlide = Math.round(scrollLeft / cardWidth);
    }
  }

  goToSlide(index: number): void {
    if (this.sliderContainer) {
      const container = this.sliderContainer.nativeElement;
      const scrollAmount = container.offsetWidth * index;
      container.scrollTo({ left: scrollAmount, behavior: 'smooth' });
    }
  }

}
