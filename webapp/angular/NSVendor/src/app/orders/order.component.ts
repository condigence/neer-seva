import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from '../service/order.service';
import { ModalService } from '../service/modal.service';
import { HttpClientModule } from '@angular/common/http';
//import { AgmCoreModule } from '@agm/core';



@Component({
  selector: 'app-my-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  orders: any;

  order: any;

  constructor(
    private orderService: OrderService,
    private modalService: ModalService,    
  ) { }

  ngOnInit() {
    this.getMyOrders();
  }

  getMyOrders() {
    let currentUser = localStorage.getItem('currentUser');
    this.orderService.getOrderByUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.orders = data;
      console.log(this.orders);
    });
  }

  deliverOrder(order: any): void {
    let currentUser = localStorage.getItem('currentUser');
    this.orderService.getOrderByUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.orders = data;
    });
  }

  updateOrder(order: any): void {
    let currentUser = localStorage.getItem('currentUser');
    this.orderService.getOrderByUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.orders = data;
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
