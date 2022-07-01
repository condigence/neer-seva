import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AddressService } from 'src/app/services/address.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'edit-address',
  templateUrl: './addressdir.html',
  styleUrls: ['./addressdir.scss']
})


export class EditAddressDir {
  editForm: FormGroup;

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
    private addressService: AddressService) {}

   ngOnInit() {
    const id = localStorage.getItem('editAddressId');
    console.log(id);
    if (!id) {
      alert('Invalid action.');
      this.router.navigate(['address-dir']);
      return;
    }
    this.editForm = this.formBuilder.group({
      type:  ['', Validators.required],
      // line1: ['', Validators.required],
      // line2: ['', Validators.required],
      // line3: ['', Validators.required],
      // line4: ['', Validators.required],
      // pin:   ['', Validators.required],
      // city:  ['', Validators.required],
      // state: ['', Validators.required],
      // country:  ['', Validators.required],
      // userId:  ['', Validators.required],
      // isDefault: ['']

    });
    // this.addressService.getAddressbyId(id)
    //   .subscribe(data => {
    //     this.editForm.setValue(data[0]);
    //   });
  }



  get f() { return this.editForm.controls; }

  onSubmit() {

    console.log(this.editForm.value);
    // this.addressService.updateAddress(this.editForm.value)
    //   .pipe(first())
    //   .subscribe(
    //     data => {
    //       this.router.navigate(['add']);
    //     },
    //     error => {
    //       alert(error);
    //     });
  }
}




