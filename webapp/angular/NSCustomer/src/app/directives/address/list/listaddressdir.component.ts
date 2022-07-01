import { Component } from '@angular/core';
import { AddressService } from 'src/app/services/address.service';
import { Address } from 'src/app/model/address.model';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'list-address-dir',
  templateUrl: './listaddressdir.component.html',
  styleUrls: ['./listaddressdir.component.scss']
})
export class ListAddressDir {
  public popoverTitle: string = 'Are You Sure to Delete??';
  public popoverMessage: string = 'You will no longer with this record';
  public confirmClicked: boolean = false;
  public cancelClicked: boolean = false;
  address;
  makedefault = [];
  constructor(
    private addressService: AddressService,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {

  }

  ngOnInit() {
    this.getMyAddressesByUserId();
  }


  getMyAddressesByUserId() {

    let currentUser = localStorage.getItem('currentUser');

    if (currentUser) {
    } else {
      console.log('Not Logged In!');
      this.router.navigate(['/login']);
    }

    let id = JSON.parse(currentUser).id;
    this.addressService.getAddressByUserId(+id)
      .subscribe(Response => {
        this.address = Response;
        console.log( this.address);
      });
  }


  // getMyDefaultAddressesByUserId() {

  //   let currentUser = localStorage.getItem('currentUser');

  //   if (currentUser) {
  //   } else {
  //     console.log('Not Logged In!');
  //     this.router.navigate(['/login']);
  //   }

  //   let id = JSON.parse(currentUser).data.id;
  //   this.addressService.getAddressByUserId(+id)
  //     .subscribe(Response => {
  //       this.address = Response;
  //       // for (let i = 0; i < this.address.length; i++) {
  //       //   this.makedefault.push(this.address[i].is_default);
  //       // }
  //     });
  // }



  // getAddressById() {
  //   this.addressService.getAddressByUserId(9)
  //     .subscribe(Response => {
  //       this.address = Response;
  //       for (let i = 0; i < this.address.length; i++) {
  //         this.makedefault.push(this.address[i].is_default);
  //       }
  //     });
  // }

  deleteAddress(address: Address) {

    this.addressService.deleteAddress(address.id)
      .subscribe(data => {
        this.getMyAddressesByUserId();
      });
  }

  updateAddress(address: Address) {
    localStorage.removeItem('editAddressId');
    localStorage.setItem('editAddressId', address.id);
    this.router.navigate(['address/edit-address']);
  }

  makeDefaultAddress(address: Address) {
    console.log(address);
    this.addressService.makeDefaultChange(address)
      .subscribe(Response => {
        window.location.reload();
      });

  }

}
