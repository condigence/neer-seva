//import { Component } from '@angular/core';

import { Component, OnChanges, OnInit, HostListener, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../services/auth.service';
import { User } from '../model/user.model';



@Component({
  selector: 'menu',
  template: `
  

  <header class="topbar-nav"  >
  <nav class="navbar navbar-expand-lg navbar-light fixed-top bg-light d-print-none" >

  <ul class="navbar-nav mr-auto align-items-center">
      <li>
          <div class="col"><a class="navbar-brand" routerLink='/products'><img src="/assets/images/lg.png"
                      class="logo mr-2" /></a>
          </div>
      </li>

  </ul>
  <ul class="navbar-nav mr-auto align-items-center">
      <li class="nav-item">
          <a class="nav-link" routerLinkActive="active" routerLink='/products'>Products</a>
      </li>
      <li class="nav-item">
          <a class="nav-link" routerLinkActive="active" routerLink='/address/list'>Address</a>
      </li>

      <li class="nav-item">
          <a class="nav-link" routerLinkActive="active" routerLink='/orders'>Orders</a>
      </li>

      <!-- <li class="nav-item">
          <a class="nav-link" routerLinkActive="active" routerLink='/profile/my-profile'>Profile</a>
      </li> -->
  </ul>
  <div *ngIf="currentUser" class="user-greeting-container">
      <div class="user-greeting">
          <span class="greeting-text">Hello,</span>
          <span class="user-name">{{currentUser.name || currentUser.contact}}</span>
          <span class="greeting-wave">👋</span>
      </div>
  </div>
  <ul class="navbar-nav align-items-center right-nav-link">
      <li class="dropdown-divider"></li>
      <li class="dropdown user-dropdown">
          <a class="dropdown-toggle" (click)="toggleDropdown()" style="cursor: pointer;">
              <div class="media" *ngIf="currentUser">
                  <div class="avatar">
                      <img *ngIf="currentUser.pic" class="align-self-start mr-3" [src]="'data:image/jpeg;base64,'+currentUser.pic"
                          alt="user avatar">
                      <i *ngIf="!currentUser.pic" class="zmdi zmdi-account-circle default-user-icon"></i>
                  </div>
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
      </li>
  </ul>

</nav>

<header>




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
    }
  `]
})


export class MenuDir  {

  userloggedOut:any;
  currentUser: any;
  isDropdownOpen: boolean = false;
  
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
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

  logout() {
    this.isDropdownOpen = false;
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
