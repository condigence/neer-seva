import { Component, OnInit } from '@angular/core';
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
  constructor(private userService: UserService) { }

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




   

}
