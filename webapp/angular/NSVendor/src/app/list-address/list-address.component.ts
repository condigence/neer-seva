import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Address } from '../model/address.model';
import { AddressService } from '../service/address.service';

@Component({
  selector: 'app-list-address',
  templateUrl: './list-address.component.html',
  styleUrls: ['./list-address.component.scss']
})
export class ListAddressComponent implements OnInit {
  public popoverTitle: string = 'Are You Sure to Delete??';
  public popoverMessage: string = 'You will no longer with this record';
  public confirmClicked: boolean = false;
  public cancelClicked: boolean = false;

  address;
  name;

  constructor(private router: Router, private addressService: AddressService) { }


  ngOnInit() {
    //this.getAllAddress();
  }

  // getAllAddress(): void {
  //   this.addressService.getAllAddress().subscribe(data => {
  //     this.address = data;
  //   });
  // }

  addAddress(): void {
    this.router.navigate(['add-address']);
  }

  // deleteAddress(address: Address) {
  //   this.addressService.deleteAddress(address.id)
  //   .subscribe(data => {
  //     this.getAllAddress();
  //   });
  // }

  updateAddress(address: Address) {
    localStorage.removeItem('editAddressId');
    localStorage.setItem('editAddressId', address.id);
    this.router.navigate(['edit-address']);
  }

  Search() {
    if (this.name !==  '') {
    } else if (this.name === '') {
      this.ngOnInit();
    }
    this.name = this.name.filter(res => {
      return res.name.toLowerCase().match(this.name.toLowerCase());
    });
  }


}
