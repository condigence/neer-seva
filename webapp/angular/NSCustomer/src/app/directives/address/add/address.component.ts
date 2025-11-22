import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressService } from 'src/app/services/address.service';
import { ToastrService } from 'ngx-toastr';

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

  addForm: FormGroup;
  submitted = false;
  isAddingAddress: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private addressService: AddressService,
    private toastr: ToastrService) { }
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
    let currentUser = localStorage.getItem('currentUser');
    this.addForm.controls['userId'].setValue(JSON.parse(currentUser).id);

    this.submitted = true;
    if (this.addForm.valid) {
      this.isAddingAddress = true;
      
      this.addressService.addAddress(this.addForm.value)
        .subscribe(
          data => {
            this.isAddingAddress = false;
            
            // Show success toast
            this.toastr.success('Address added successfully!', 'Success');
            
            // Navigate to address list
            setTimeout(() => {
              this.router.navigate(['/address/list']);
            }, 500);
          },
          error => {
            console.error('Error adding address:', error);
            this.isAddingAddress = false;
            this.toastr.error('Failed to add address. Please try again.', 'Error');
          }
        );
    } else {
      console.log("Not valid");
      this.toastr.warning('Please fill all required fields.', 'Form Invalid');
    }
  }

  get f() { return this.addForm.controls; }
}
