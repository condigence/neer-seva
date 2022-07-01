import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService) { }
  user;
  ngOnInit() {

    const currentUser = localStorage.getItem('currentUser');
    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;

      console.log(this.user);
    });

  }

}
