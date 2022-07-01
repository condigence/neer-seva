import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './service/auth.service';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  user: any;
  isUserActive: any;
  title = 'NeerSevaAdmin';
  currentUser: any;  // Sol. for data.id property does not exist

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));    
  }

  ngOnInit(): void {

    const currentUser = localStorage.getItem('currentUser');

    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;
      if (this.user.active) {
        this.isUserActive = true;

        console.log(this.user);
       // console.log(this.isUserActive);
      } else {
        this.isUserActive = false;
        //console.log(this.isUserActive);
        console.log("Please complete Profile!");
      }
    });
   // console.log(this.currentUser);
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
