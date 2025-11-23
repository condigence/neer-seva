import { Component, OnInit } from '@angular/core';
//import { UserView } from 'src/app/viewmodel/user.view.model';
//import { ProfileService } from 'src/app/service/profile.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ProfileService } from 'src/app/services/profile.service';
import { OrderService } from '../../services/order.service';
import { ModalService } from '../../modal';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  orders: any[] = [];
  order: any;
  currentUser: any;
  isLoading: boolean = false;
  hasError: boolean = false;

  constructor(
    private orderService: OrderService,
    private modalService: ModalService,
    private router: Router,
    private toastr: ToastrService
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
    this.isLoading = true;
    this.hasError = false;
    let currentUser = localStorage.getItem('currentUser');
    this.orderService.getOrderByUserById(JSON.parse(currentUser).id).subscribe(
      data => {
        this.isLoading = false;
        console.log('raw orders response:', data);
        if (Array.isArray(data)) {
          this.orders = data;
        } else if (data && Array.isArray(data.data)) {
          this.orders = data.data;
        } else if (data && Array.isArray(data.orders)) {
          this.orders = data.orders;
        } else if (data && Array.isArray(data.items)) {
          this.orders = data.items;
        } else {
          // fallback: coerce single object to an array or keep empty
          this.orders = data ? [data] : [];
        }
        console.log('normalized orders:', this.orders);
        
        // Show message if no orders found
        if (this.orders.length === 0) {
          this.toastr.info('No orders found. Start shopping to place your first order!', 'No Orders', {
            timeOut: 5000,
            progressBar: true
          });
        }
      },
      err => {
        this.isLoading = false;
        this.hasError = true;
        console.error('Failed to load orders', err);
        this.orders = [];
        this.toastr.error('Failed to load orders. Please try again later.', 'Error', {
          timeOut: 5000,
          progressBar: true
        });
      }
    );
  }

  openModal(id: string, order?: any) {
    console.log(order);
    const modalRef = this.modalService.open(id, order || null);
    this.order = order;
  }

  

  closeModal(id: string) {
    this.modalService.close(id);
  }








}
