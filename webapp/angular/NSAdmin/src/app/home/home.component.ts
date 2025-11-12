import { Component, OnInit, Renderer2 } from '@angular/core';
import { UserService } from '../service/user.service';
import { AddressService } from '../service/address.service';

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
  top4Orders: any;
  fiveUsers: any;
  constructor(private userService: UserService, private addressService: AddressService, private renderer: Renderer2) { }
  

  ngOnInit() {
    this.userService.getAllUsers()
    .subscribe(data => {
      this.userscount = data;  
       
    });
    this.userService.getTop5CustomerCount()
    .subscribe(data => {
      this.userAsCustomer = data;   

   
    });
    this.userService.getTop5VendorCount()
    .subscribe(data => {
      this.userAsVendor = data; 
       // Loop through each vendor and get their address
    this.userAsVendor.forEach(vendor => {
    this.addressService.getAddressById(vendor.addressId).subscribe(address => {
      vendor.address = address; // Attach address to vendor object
    });
  });
  
    });

    
    

    this.userService.getAllOrderCount()
    .subscribe(data => {
      this.totalOrders = data;  
    this.top4Orders = [...this.totalOrders].reverse().slice(0, 4);

  console.log(this.top4Orders);
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
