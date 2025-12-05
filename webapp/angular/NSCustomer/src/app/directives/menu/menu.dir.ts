import { Component, OnChanges, OnInit, HostListener, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';


@Component({
  selector: 'menu',
    templateUrl: './menu.dir.html',
    styleUrls: ['./menu.dir.scss']
})



export class MenuDir  {

  userloggedOut:any;
  currentUser: any;
  isDropdownOpen: boolean = false;
  isMobileMenuOpen: boolean = false;
  
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private cartService: CartService,
    private elementRef: ElementRef
  ) {


    this.authenticationService.currentUser
    .subscribe(Response => {
        this.currentUser = Response;
    });
   
  }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }



  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    // Close dropdown if click is outside the dropdown element
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isDropdownOpen = false;
    }
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    // Prevent body scroll when menu is open
    if (this.isMobileMenuOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }
  }

  closeMobileMenu() {
    this.isMobileMenuOpen = false;
    document.body.style.overflow = 'auto';
  }

  logout() {
    this.isDropdownOpen = false;
    this.closeMobileMenu();    
    // Clear cart before logout
    this.cartService.clearCart();    
    // Logout user
    this.authenticationService.logout();    
    // Navigate to products page (home page)
    this.router.navigate(['/products']);
  }
}
