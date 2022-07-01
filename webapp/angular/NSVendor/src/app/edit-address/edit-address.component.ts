
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ItemService } from '../service/item.service';
import { AddressService } from '../service/address.service';
import { first } from 'rxjs/operators';
import { Address } from '../model/address.model';


@Component({
  selector: 'app-edit-address',
  templateUrl: './edit-address.component.html',
  styleUrls: ['./edit-address.component.scss']
})
export class EditAddressComponent implements OnInit {

  address: Address;
  editForm: FormGroup;
  submitted = false;

  addressType = [
    { addressName: 'Home' },
    { addressName: 'Office' },
    { addressName: 'Parmanent' },
    { addressName: 'Temporary' },
    { addressName: 'Village' },
    { addressName: 'Others' }
  ];


  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private addressService: AddressService
  ) { }

  ngOnInit() {
    const address_id = localStorage.getItem('editAddressId');
    if (!address_id) {
      alert('Invalid action.');
      this.router.navigate(['list-address']);
      return;
    }
    this.editForm = this.formBuilder.group({
      address_id: ['', Validators.required],
      address_type: ['', Validators.required],
      addr_line1: ['', Validators.required],
      addr_line2: ['', Validators.required],
      addr_line3: ['', Validators.required],
      addr_line4: ['', Validators.required],
      pin: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', Validators.required],
      location_long: ['', Validators.required],
      location_latt: ['', Validators.required],
      location_name: ['', Validators.required],
      user_id: ['', Validators.required]
    });
    // this.addressService.getAddressById(+address_id)
    //   .subscribe(data => {
    //     this.editForm.setValue(data[0]);
    //   });
  }



  get f() { return this.editForm.controls; }

  onSubmit() {
    this.addressService.updateAddress(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['list-address']);
        },
        error => {
          alert(error);
        });
  }
}
