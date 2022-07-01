import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { OrderService } from '../service/order.service';
import { AuthenticationService } from '../service/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  usercount: any;
  userAsCustomer: any;
  userAsVendor: any;
  totalOrders: any;
  currentUser: any;
  constructor(
    private userService: UserService,
    private orderService: OrderService,
    private authenticationService: AuthenticationService
    ) { 
      this.authenticationService.currentUser.subscribe(x => this.currentUser = x);           
    }

  ngOnInit() {
    this.userService.getAllUsersCount()
    .subscribe(data => {
      this.usercount = data;         
    });
    this.userService.getAllCustomerCount()
    .subscribe(data => {
      this.userAsCustomer = data;      
    });
    this.userService.getAllVendorCount()
    .subscribe(data => {
      this.userAsVendor = data;      
    });
    this.orderService.getNumberOfOrdersByVendorId(this.currentUser.id)
    .subscribe(data => {
      this.totalOrders = data;              
    });
  }

}
