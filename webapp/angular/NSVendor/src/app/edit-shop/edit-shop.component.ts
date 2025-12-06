import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';
import { ShopService } from '../service/shop.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-edit-shop',
  templateUrl: './edit-shop.component.html',
  styleUrls: ['./edit-shop.component.scss']
})
export class EditShopComponent implements OnInit {

  shopTypes = [
    {type: 'kirana'},
    {type: 'Genral store'},
    {type: 'Vegitable Shop'},
    {type: 'Water Delivery shop'},
    {type: 'Milk & Water shop'},
    {type: 'Milk & Vegitable Delivery shop'},
  ];
    editForm: FormGroup;
    submitted = false;

    imageId: number;
    messageToSendP: string = 'SHOP';

    constructor(private formBuilder: FormBuilder, private router: Router, private shopService: ShopService) { }

    ngOnInit() {
      const id = localStorage.getItem('editShopId');
      if (!id) {
        alert('Invalid action.');
        this.router.navigate(['list-shop']);
        return;
      }
      this.editForm = this.formBuilder.group({
        id: [],
        name: ['', Validators.required],
        imageId: ['', Validators.required],
        type: ['', Validators.required],
        addressId: ['', Validators.required],
        code: ['', Validators.required],
        branch: ['', Validators.required],
        userId: ['', Validators.required],
        pic: ['']
      });
      
      let currentUser = localStorage.getItem('currentUser');
    //  this.shopService.getShopByVendorId(JSON.parse(currentUser).id)

      this.shopService.getShopById(+id)
        .subscribe(data => {  
        this.editForm.setValue(data);
    });
  }

  receiveMessage($event) {
    this.imageId = $event;
    // console.log(this.imageId , "line no 62 edit shop comp", $event);
    this.editForm.controls['imageId'].setValue(this.imageId);
  }

  get f() { return this.editForm.controls; }

  onSubmit() {

    console.log(this.editForm.value);

    this.shopService.updateShop(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Shop Updated',
            showConfirmButton: true,
            timer: 3000,
            timerProgressBar: true,
          });
          this.router.navigate(['list-shop']);
        },
        error => {
          alert(error);
        });
  }



}
