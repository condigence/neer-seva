import { Component, OnInit, Renderer2 } from '@angular/core';
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

  constructor(private userService: UserService, private router: Router, private renderer: Renderer2) { }
  user: User = new User;
  ngOnInit() {

    const currentUser = localStorage.getItem('currentUser');
    this.currentUser = JSON.parse(currentUser || 'null');
    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;

      console.log(this.user);
    });

  }

  toggleSidebar() {
    // toggle the wrapper.toggled class to show/hide the sidebar
    const wrapper = document.getElementById('wrapper');
    if (wrapper) {
      if (wrapper.classList.contains('toggled')) {
        this.renderer.removeClass(wrapper, 'toggled');
        const overlay = document.getElementById('sidebar-overlay');
        if (overlay) { this.renderer.removeClass(overlay, 'visible'); }
      } else {
        this.renderer.addClass(wrapper, 'toggled');
        const overlay = document.getElementById('sidebar-overlay');
        if (overlay) { this.renderer.addClass(overlay, 'visible'); }
      }
    }
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

}
