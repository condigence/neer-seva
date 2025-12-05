
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

  @ViewChild('gridContainer') gridContainer: ElementRef;
  outlets: any;
  // outlets:Shop = [];
  outletId: number;
  imageModuleName: string;
  currentPage: number = 1;
  private screenWidth: number = window.innerWidth;

  constructor(
    public userService: UserService
  ) {

  }

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();

  ngOnInit() {
    this.getAllOutlets();
    this.updateScreenWidth();
    window.addEventListener('resize', () => this.updateScreenWidth());
  }
  
  updateScreenWidth(): void {
    const oldCardsPerPage = this.getCardsPerPage();
    this.screenWidth = window.innerWidth;
    const newCardsPerPage = this.getCardsPerPage();
    
    // Reset to page 1 if cards per page changed to avoid empty pages
    if (oldCardsPerPage !== newCardsPerPage) {
      this.currentPage = 1;
    }
  }
  
  getCardsPerPage(): number {
    if (this.screenWidth < 768) {
      return 1; // Mobile: 1 card
    } else if (this.screenWidth < 992) {
      return 2; // Tablet portrait: 2 cards
    } else if (this.screenWidth < 1200) {
      return 3; // Tablet landscape: 3 cards
    } else {
      return 4; // Desktop: 4 cards
    }
  }
  getAllOutlets() {
    this.userService.getAllOutlets().subscribe(data => {
      this.outlets = data;    
    });
  }

  getShopItemsByShopId(shop: any): void {
    // Emit the shop ID - products page will handle storage and loading
    this.messageEvent.emit(shop.id);
  }
  
  isShopSelected(shopId: any): boolean {
    const selectedShop = localStorage.getItem('selectedShop');
    return selectedShop === String(shopId);
  }
  
  getCurrentPageVendors(): any[] {
    if (!this.outlets || this.outlets.length === 0) return [];
    const cardsPerPage = this.getCardsPerPage();
    const startIndex = (this.currentPage - 1) * cardsPerPage;
    const endIndex = startIndex + cardsPerPage;
    return this.outlets.slice(startIndex, endIndex);
  }

  get totalPages(): number {
    if (!this.outlets || this.outlets.length === 0) return 1;
    return Math.ceil(this.outlets.length / this.getCardsPerPage());
  }

  getPageArray(): number[] {
    return Array(this.totalPages).fill(0).map((_, i) => i + 1);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

}
