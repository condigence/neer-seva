import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  currentUser: any;

  constructor(private userService: UserService, private router: Router) { }
  user: User = new User;
  ngOnInit() {

    const currentUser = localStorage.getItem('currentUser');
    this.currentUser = JSON.parse(currentUser || 'null');
    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;

      console.log(this.user);
    });

  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

}
