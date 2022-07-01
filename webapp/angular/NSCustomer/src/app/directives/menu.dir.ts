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
  <div *ngIf="currentUser">
      <h6 class="mt-2 user-title">Hello {{currentUser.name}}</h6>
  </div>
  <ul class="navbar-nav align-items-center right-nav-link">
      <li class="dropdown-divider"></li>
      <li class="dropdown-item user-details">
          <a>
              <div class="media" *ngIf="currentUser">
                  <div class="avatar"><img class="align-self-start mr-3" [src]="'data:image/jpeg;base64,'+currentUser.pic"
                          alt="user avatar"></div>
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
