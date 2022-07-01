import { Component, OnInit } from '@angular/core';
import { ShopService } from '../service/shop.service';
import { Router } from '@angular/router';
import { Shop } from '../model/shop.model';
import { AuthenticationService } from '../service/auth.service';
import { UserService } from '../service/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-shop',
  templateUrl: './list-shop.component.html',
  styleUrls: ['./list-shop.component.scss']
})
export class ListShopComponent implements OnInit {

  public popoverTitle: string = 'Are You Sure to Delete??';
  public popoverMessage: string = 'You will no longer with this record';
  public confirmClicked: boolean = false;
  public cancelClicked: boolean = false;

  shops:any;
  name: string;
  currentUser: any;
  user: any;
  constructor(
    private router: Router,
    private shopService: ShopService,
    private userService: UserService,
    private authenticationService: AuthenticationService
    ) {
      this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
      this.userService.getUserById(this.currentUser.id).subscribe(data => {
        this.user = data;
      });      
    }

  ngOnInit() {
    this.getAllShops(this.currentUser.id);
  }

  getAllShops(id): void {
    this.shopService.
    getShopByVendorId(id)
      .subscribe(data => {
      this.shops = data;      
    });
  }
  
  deleteTheShop(shop: Shop) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this Shop',
      icon: 'warning',
      showCancelButton: true,
      cancelButtonColor: '#3085d6',
      confirmButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.value) {
        this.shopService.deleteShop(shop.id)
    .subscribe(data => {
      this.getAllShops(this.currentUser.id);
    });
        Swal.fire(
        'Deleted!',
        `<strong style="color:red;">Your selected shop has been deleted.</strong>`,
        'success'
      );

      } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire(
        'Cancelled',
        'Your selected shop has been safe :)',
        'error'
      );
      }
    });
    
    
  }

  updateShop(shop: Shop) {
    console.log(shop);
    localStorage.removeItem('editShopId');
    localStorage.setItem('editShopId',  String(shop.id));
    this.router.navigate(['edit-shop']);
  }

  Search() {
    if (this.name !==  '') {
    } else if (this.name === '') {
      this.ngOnInit();
    }
    this.shops = this.shops.filter(res => {
      return res.name.toLowerCase().match(this.name.toLowerCase());
    });
  }

}
