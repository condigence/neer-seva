import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressService } from 'src/app/services/address.service';

@Component({
  selector: 'add-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.scss']
})
export class AddAddressDir implements OnInit {


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
    private addressService: AddressService) { }

  addForm: FormGroup;
  submitted = false;
  ngOnInit() {
    this.addForm = this.formBuilder.group({
      type:  ['', Validators.required],
      line1: ['', Validators.required],
      line2: ['', Validators.required],
      line3: ['', Validators.required],
      line4: ['', Validators.required],
      pin:   ['', Validators.required],
      city:  ['', Validators.required],
      state: ['', Validators.required],
      country:  ['', Validators.required],
      userId:  ['', Validators.required],
      isDefault: ['']

      
    });

    
  }

  onSubmit() {

     console.log(this.addForm.value); 
    
    let currentUser = localStorage.getItem('currentUser');
    console.log('submitted!'+JSON.parse(currentUser).id);
    this.addForm.controls['userId'].setValue(JSON.parse(currentUser).id);

    console.log(this.addForm.valid);
    this.submitted = true;
    if (this.addForm.valid) {
      console.log("valid");
      this.addressService.addAddress(this.addForm.value)
        .subscribe(data => {
          this.router.navigate(['/']);
        });
    }else{
      console.log("Not valid");
    }
  }


  get f() { return this.addForm.controls; }


}
