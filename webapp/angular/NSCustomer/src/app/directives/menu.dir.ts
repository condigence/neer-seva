//import { Component } from '@angular/core';

import { Component, OnChanges, OnInit, HostListener, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../services/auth.service';
import { CartService } from '../services/cart.service';
import { User } from '../model/user.model';



@Component({
  selector: 'menu',
  template: `
  <header class="topbar-nav">
  <nav class="navbar navbar-expand-lg navbar-light fixed-top bg-light d-print-none">

  <!-- Logo -->
  <a class="navbar-brand" routerLink='/products'>
      <img src="/assets/images/lg.png" class="logo" alt="Logo" />
  </a>

  <!-- Mobile Menu Toggle -->
  <button class="mobile-menu-toggle" (click)="toggleMobileMenu()" type="button" [class.open]="isMobileMenuOpen">
      <span class="toggle-icon">
          <span></span>
          <span></span>
          <span></span>
      </span>
  </button>

  <!-- Desktop Navigation -->
  <div class="desktop-nav">
      <ul class="navbar-nav nav-links">
          <li class="nav-item">
              <a class="nav-link" routerLinkActive="active" routerLink='/products'>Products</a>
          </li>
          <li class="nav-item">
              <a class="nav-link" routerLinkActive="active" routerLink='/address/list'>Address</a>
          </li>
          <li class="nav-item">
              <a class="nav-link" routerLinkActive="active" routerLink='/orders'>Orders</a>
          </li>
      </ul>

      <div class="nav-right">
          <div *ngIf="currentUser" class="user-greeting-container">
              <div class="user-greeting">
                  <span class="greeting-text">Hello,</span>
                  <span class="user-name">{{currentUser.name || currentUser.contact}}</span>
                  <span class="greeting-wave">👋</span>
              </div>
          </div>

          <div class="user-dropdown" *ngIf="currentUser">
              <a class="dropdown-toggle" (click)="toggleDropdown()" style="cursor: pointer;">
                  <div class="avatar">
                      <img *ngIf="currentUser.pic" [src]="'data:image/jpeg;base64,'+currentUser.pic" alt="user avatar">
                      <i *ngIf="!currentUser.pic" class="zmdi zmdi-account-circle default-user-icon"></i>
                  </div>
              </a>
              <div class="dropdown-menu-custom" [class.show]="isDropdownOpen">
                  <a class="dropdown-item-custom" routerLink="/profile/my-profile" (click)="closeDropdown()">
                      <i class="zmdi zmdi-account"></i> Profile
                  </a>
                  <div class="dropdown-divider"></div>
                  <a class="dropdown-item-custom" (click)="closeDropdown()">
                      <i class="zmdi zmdi-settings"></i> Preferences
                  </a>
                  <div class="dropdown-divider"></div>
                  <a class="dropdown-item-custom" (click)="logout()">
                      <i class="zmdi zmdi-power"></i> Logout
                  </a>
              </div>
          </div>
      </div>
  </div>

  <!-- Mobile Menu Overlay -->
  <div class="mobile-menu-overlay" [class.show]="isMobileMenuOpen" (click)="closeMobileMenu()"></div>
  
  <!-- Mobile Menu -->
  <div class="mobile-menu" [class.show]="isMobileMenuOpen">
      <div class="mobile-menu-header">
          <h5 *ngIf="currentUser">Hello, {{currentUser.name || currentUser.contact}}! 👋</h5>
      </div>
      
      <ul class="mobile-nav-links">
          <li>
              <a routerLink='/products' (click)="closeMobileMenu()" routerLinkActive="active">
                  <i class="zmdi zmdi-shopping-basket"></i>
                  <span>Products</span>
              </a>
          </li>
          <li>
              <a routerLink='/address/list' (click)="closeMobileMenu()" routerLinkActive="active">
                  <i class="zmdi zmdi-pin"></i>
                  <span>Address</span>
              </a>
          </li>
          <li>
              <a routerLink='/orders' (click)="closeMobileMenu()" routerLinkActive="active">
                  <i class="zmdi zmdi-shopping-cart"></i>
                  <span>Orders</span>
              </a>
          </li>
          <li>
              <a routerLink='/profile/my-profile' (click)="closeMobileMenu()" routerLinkActive="active">
                  <i class="zmdi zmdi-account"></i>
                  <span>Profile</span>
              </a>
          </li>
          <li>
              <a (click)="logout()" style="cursor: pointer;">
                  <i class="zmdi zmdi-power"></i>
                  <span>Logout</span>
              </a>
          </li>
      </ul>
  </div>

</nav>
</header>




  `,

  styles: [`
    .logo{
      height: 60px;
      width: auto;
    }

    /* Beautiful Navbar Menu Styling */
    .navbar {
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
      padding: 10px 20px;
    }

    .nav-item {
      margin: 0 5px;
    }

    .nav-link {
      position: relative;
      padding: 10px 20px !important;
      color: #555 !important;
      font-weight: 600;
      font-size: 0.95rem;
      letter-spacing: 0.3px;
      transition: all 0.3s ease;
      border-radius: 8px;
    }

    .nav-link::before {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 0;
      height: 3px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      transition: width 0.3s ease;
      border-radius: 3px;
    }

    .nav-link:hover {
      color: #667eea !important;
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
    }

    .nav-link:hover::before {
      width: 70%;
    }

    .nav-link.active {
      color: #667eea !important;
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.12) 0%, rgba(118, 75, 162, 0.12) 100%);
      box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
    }

    .nav-link.active::before {
      width: 70%;
    }

    .user-greeting-container {
      margin-right: 20px;
      display: flex;
      align-items: center;
    }

    .right-nav-link {
      display: flex;
      align-items: center;
      gap: 0;
    }

    .user-greeting {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 10px 20px;
      border-radius: 25px;
      display: flex;
      align-items: center;
      gap: 8px;
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
      transition: all 0.3s ease;
      cursor: pointer;
    }

    .user-greeting:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
    }

    .greeting-text {
      color: rgba(255, 255, 255, 0.9);
      font-size: 0.9rem;
      font-weight: 400;
    }

    .user-name {
      color: #ffffff;
      font-size: 1rem;
      font-weight: 700;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    }

    .greeting-wave {
      font-size: 1.2rem;
      animation: wave 2s ease-in-out infinite;
      transform-origin: 70% 70%;
    }

    @keyframes wave {
      0%, 100% {
        transform: rotate(0deg);
      }
      10%, 30% {
        transform: rotate(14deg);
      }
      20% {
        transform: rotate(-8deg);
      }
      40% {
        transform: rotate(-4deg);
      }
      50% {
        transform: rotate(10deg);
      }
      60% {
        transform: rotate(0deg);
      }
    }

    /* Responsive */
    .avatar {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .avatar img {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      object-fit: cover;
    }

    .default-user-icon {
      font-size: 40px;
      color: #667eea;
    }

    /* Dropdown Styling */
    .user-dropdown {
      position: relative;
      display: flex;
      align-items: center;
    }

    .dropdown-toggle {
      display: block;
    }

    .dropdown-menu-custom {
      position: absolute;
      top: 100%;
      right: 0;
      margin-top: 10px;
      background: white;
      border-radius: 12px;
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      min-width: 200px;
      opacity: 0;
      visibility: hidden;
      transform: translateY(-10px);
      transition: all 0.3s ease;
      z-index: 1000;
      overflow: hidden;
    }

    .dropdown-menu-custom.show {
      opacity: 1;
      visibility: visible;
      transform: translateY(0);
    }

    .dropdown-menu-custom::before {
      content: '';
      position: absolute;
      top: -8px;
      right: 20px;
      width: 0;
      height: 0;
      border-left: 8px solid transparent;
      border-right: 8px solid transparent;
      border-bottom: 8px solid white;
    }

    .dropdown-item-custom {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 20px;
      color: #333;
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s ease;
      font-size: 0.95rem;
      font-weight: 500;
    }

    .dropdown-item-custom:hover {
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
      color: #667eea;
    }

    .dropdown-item-custom i {
      font-size: 18px;
      color: #667eea;
    }

    .dropdown-menu-custom .dropdown-divider {
      margin: 0;
      border-top: 1px solid #e0e0e0;
    }

    /* Mobile Menu Toggle Button */
    .mobile-menu-toggle {
      display: none;
      background: none;
      border: none;
      padding: 10px;
      cursor: pointer;
      z-index: 1001;
      position: relative;
    }

    .toggle-icon {
      display: flex;
      flex-direction: column;
      gap: 5px;
      width: 28px;
      height: 22px;
      justify-content: center;
      position: relative;
    }

    .toggle-icon span {
      display: block;
      width: 100%;
      height: 3px;
      background: #667eea;
      border-radius: 3px;
      transition: all 0.3s ease;
      position: relative;
    }

    .mobile-menu-toggle.open .toggle-icon span:nth-child(1) {
      transform: rotate(45deg) translate(8px, 8px);
    }

    .mobile-menu-toggle.open .toggle-icon span:nth-child(2) {
      opacity: 0;
    }

    .mobile-menu-toggle.open .toggle-icon span:nth-child(3) {
      transform: rotate(-45deg) translate(8px, -8px);
    }

    /* Desktop Navigation */
    .desktop-nav {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex: 1;
      margin-left: 30px;
    }

    .nav-links {
      display: flex;
      flex-direction: row;
      gap: 5px;
      margin: 0;
      padding: 0;
      list-style: none;
    }

    .nav-right {
      display: flex;
      align-items: center;
      gap: 15px;
    }

    /* Mobile Menu Overlay */
    .mobile-menu-overlay {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      z-index: 999;
      opacity: 0;
      visibility: hidden;
      transition: all 0.3s ease;
    }

    .mobile-menu-overlay.show {
      opacity: 1;
      visibility: visible;
    }

    /* Mobile Menu */
    .mobile-menu {
      display: none;
      position: fixed;
      top: 0;
      right: -300px;
      width: 280px;
      height: 100%;
      background: white;
      box-shadow: -5px 0 25px rgba(0, 0, 0, 0.2);
      z-index: 1000;
      transition: right 0.3s ease;
      overflow-y: auto;
    }

    .mobile-menu.show {
      right: 0;
    }

    .mobile-menu-header {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }

    .mobile-menu-header h5 {
      margin: 0;
      font-size: 1rem;
      font-weight: 600;
      text-align: center;
    }

    .mobile-nav-links {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    .mobile-nav-links li {
      border-bottom: 1px solid #f0f0f0;
    }

    .mobile-nav-links a {
      display: flex;
      align-items: center;
      gap: 15px;
      padding: 18px 20px;
      color: #333;
      text-decoration: none;
      transition: all 0.3s ease;
      font-weight: 500;
    }

    .mobile-nav-links a:hover {
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
      color: #667eea;
    }

    .mobile-nav-links a.active {
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.15) 0%, rgba(118, 75, 162, 0.15) 100%);
      color: #667eea;
      border-left: 4px solid #667eea;
    }

    .mobile-nav-links a i {
      font-size: 22px;
      color: #667eea;
    }

    .mobile-nav-links a span {
      flex: 1;
    }

    /* Responsive Breakpoints */
    @media (max-width: 991px) {
      .mobile-menu-toggle {
        display: block;
      }

      .desktop-nav {
        display: none;
      }

      .mobile-menu,
      .mobile-menu-overlay {
        display: block;
      }

      .navbar {
        padding: 12px 15px;
      }

      .logo {
        height: 45px;
      }
    }

    @media (max-width: 768px) {
      .user-greeting {
        padding: 8px 15px;
      }

      .greeting-text {
        font-size: 0.8rem;
      }

      .user-name {
        font-size: 0.9rem;
      }

      .greeting-wave {
        font-size: 1rem;
      }

      .logo {
        height: 40px;
      }
    }

    @media (max-width: 480px) {
      .mobile-menu {
        width: 100%;
        right: -100%;
      }

      .logo {
        height: 35px;
      }
    }
  `]
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
    
    // Navigate to login
    this.router.navigate(['/login']);
  }
}
