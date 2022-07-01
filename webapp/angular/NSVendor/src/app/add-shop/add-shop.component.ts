import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ShopService } from '../service/shop.service';
import { AuthenticationService } from '../service/auth.service';
import { UserService } from '../service/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-shop',
  templateUrl: './add-shop.component.html',
  styleUrls: ['./add-shop.component.scss']
})
export class AddShopComponent implements OnInit {
  mandatoryFields = '*Mandatory fields';
  addressId: number;


  shopTypes = [
    {type: 'kirana'},
    {type: 'Genral store'},
    {type: 'Vegitable Shop'},
    {type: 'Water Delivery shop'},
    {type: 'Milk & Water shop'},
    {type: 'Milk & Vegitable Delivery shop'},
  ];
  addForm: FormGroup;
  submitted = false;
  currentUser;
  profile;

  imageId: number;
  messageToSendP: string = 'SHOP';
  messageToSendA: string = 'ADDRESS';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private shopService: ShopService,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    localStorage.getItem('currentUser');
    this.userService.getUserById(this.currentUser.id).subscribe(data => {
      this.profile = data;
      });
   }



  ngOnInit() {
    this.addForm = this.formBuilder.group({
        name: ['', Validators.required],
        type: ['', Validators.required],
        // branch: ['', Validators.required],
        imageId: ['', Validators.required],
        userId: [this.currentUser.id, Validators.required],        
        addressId: [this.addressId, Validators.required],
    });
    console.log(this.currentUser.id);
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.addForm.controls['imageId'].setValue(this.imageId);
  }
  receiveMessageByAddressComponent($event) {
    this.addressId = $event;
    this.addForm.controls["addressId"].setValue(this.addressId);
    console.log(this.addressId);
  }

  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.addForm.controls[controlName].hasError(errorName);
  }

  changeUserType(e: { target: { value: any; }; }) {
    this.addForm.get('type').setValue(e.target.value, {
      onlySelf: true,
    });
  }


  onSubmit() {
    console.log(this.addForm.valid);
    console.log(this.addForm.value);
    this.submitted = true;
    console.log(this.addForm.value);
    if (this.addForm.valid) {
      this.shopService.addShop(this.addForm.value)
        .subscribe(data => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Shop has been added',
            text: 'Successfully',
            showConfirmButton: true,
            timer: 3000,
            timerProgressBar: true,
          });
          this.router.navigate(['list-shop']);
        });
    }
  }

  get f() { return this.addForm.controls; }

}
