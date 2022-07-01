
import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressService } from '../service/address.service';
import { AuthenticationService } from '../service/auth.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {

  receivedAddressData: any;
  addrressId: number;
  currentUser: any;
  profile: any;

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();





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
    private addressService: AddressService,
    private authenticationService: AuthenticationService,
    private userService: UserService
    ) {
      this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
      console.log(this.currentUser);
     }

  brands;
  addForm: FormGroup;
  submitted = false;
  ngOnInit() {
    this.addForm = this.formBuilder.group({
      address_type: ['', Validators.required],
      addr_line1: ['', Validators.required],
      addr_line2: ['', Validators.required],
      addr_line3: ['', Validators.required],
      addr_line4: ['', Validators.required],
      pin: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', Validators.required],
      user_id: [this.currentUser.id]
    });
  }


  get f() { return this.addForm.controls; }

  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.addForm.controls[controlName].hasError(errorName);
  }

  changeAddressType(e: { target: { value: any; }; }) {
    this.addForm.get('address_type').setValue(e.target.value, {
      onlySelf: true,
    });
  }


  onSubmit() {
    console.log(this.addForm.value);
    if (this.addForm.valid) {
      this.addressService.addAddress(this.addForm.value).subscribe((data) => {
        this.receivedAddressData = data;
        this.addrressId = this.receivedAddressData.id;
        //Emit Data
        this.messageEvent.emit(this.addrressId);
        console.log(this.receivedAddressData.id);
      });
    } else {
      console.log("Not valid");
    }
  }


  


}

