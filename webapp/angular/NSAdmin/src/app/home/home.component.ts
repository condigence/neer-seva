import { Component, OnInit, Renderer2 } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  
  userscount: any;
  userAsCustomer: any;
  userAsVendor: any;
  totalOrders: any;
  fiveUsers: any;
  constructor(private userService: UserService, private renderer: Renderer2) { }

  ngOnInit() {
    this.userService.getAllUsersCount()
    .subscribe(data => {
      this.userscount = data;         
    });
    this.userService.getAllCustomerCount()
    .subscribe(data => {
      this.userAsCustomer = data;      
    });
    this.userService.getAllVendorCount()
    .subscribe(data => {
      this.userAsVendor = data;      
    });
    this.userService.getAllOrderCount()
    .subscribe(data => {
      this.totalOrders = data;      
    });    
  }




   

  
  // Close dropdowns immediately when a dropdown item is clicked
  public closeOpenDropdowns(): void {
    try {
      const openMenus = document.querySelectorAll('.dropdown-menu.show');
      openMenus.forEach(menu => this.renderer.removeClass(menu, 'show'));

      const openParents = document.querySelectorAll('.dropdown.show');
      openParents.forEach(parent => this.renderer.removeClass(parent, 'show'));

      const toggles = document.querySelectorAll('.dropdown-toggle[aria-expanded]');
      toggles.forEach(t => this.renderer.setAttribute(t as HTMLElement, 'aria-expanded', 'false'));
    } catch (e) {
      // ignore
    }
  }

}
