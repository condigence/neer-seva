import { Component, OnInit } from '@angular/core';
//import { UserView } from 'src/app/viewmodel/user.view.model';
//import { ProfileService } from 'src/app/service/profile.service';
import { HttpClient } from 'selenium-webdriver/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ProfileService } from 'src/app/services/profile.service';
import { OrderService } from '../../services/order.service';
import { ModalService } from '../../modal';

@Component({
  selector: 'app-my-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  orders: any;
  order: any;
  currentUser: any;

  constructor(
    private orderService: OrderService,
    private modalService: ModalService,
    private router: Router
  ) { this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  //console.log(this.currentUser);
 // this.getMyOrders();
 }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
   // console.log(this.currentUser);
    this.getMyOrders();
  }


  getMyOrders(): void {
    let currentUser = localStorage.getItem('currentUser');
    this.orderService.getOrderByUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.orders = data;
      console.log(this.orders);
    });
  }

  openModal(id: string, order: any) {
    console.log(order)
    const modalRef = this.modalService.open(id, order);
    this.order = order;
  }

  

  closeModal(id: string) {
    this.modalService.close(id);
  }








}
