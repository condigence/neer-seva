//import { Component } from '@angular/core';

import { Component, OnChanges, OnInit } from '@angular/core';
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

      <li class="nav-item">
          <a class="nav-link" routerLinkActive="active" routerLink='/profile/my-profile'>Profile</a>
      </li>
      <li class="nav-item" *ngIf="currentUser">
          <a class="nav-link" routerLinkActive="active" (click)="logout()" routerLink='/checkout'>Logout</a>
      </li>
  </ul>
  <div *ngIf="currentUser" class="user-greeting-container">
      <div class="user-greeting">
          <span class="greeting-text">Hello,</span>
          <span class="user-name">{{currentUser.name}}</span>
          <span class="greeting-wave">👋</span>
      </div>
  </div>
  <ul class="navbar-nav align-items-center right-nav-link">
      <li class="dropdown-divider"></li>
      <li class="dropdown-item user-details">
          <a>
              <div class="media" *ngIf="currentUser">
                  <div class="avatar">
                      <img *ngIf="currentUser.pic" class="align-self-start mr-3" [src]="'data:image/jpeg;base64,'+currentUser.pic"
                          alt="user avatar">
                      <i *ngIf="!currentUser.pic" class="zmdi zmdi-account-circle default-user-icon"></i>
                  </div>
                  <div class="media-body">
                      
                  </div>
              </div>
          </a>
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

    .user-greeting-container {
      margin-right: 15px;
      display: flex;
      align-items: center;
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
    .default-user-icon {
      font-size: 40px;
      color: #667eea;
      margin-right: 12px;
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
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {


    this.authenticationService.currentUser
    .subscribe(Response => {
        this.currentUser = Response;
    });
   
  }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }



  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
