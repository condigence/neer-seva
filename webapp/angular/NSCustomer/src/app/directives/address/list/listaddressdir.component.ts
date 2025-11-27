import { Component } from '@angular/core';
import { AddressService } from 'src/app/services/address.service';
import { Address } from 'src/app/model/address.model';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/auth.service';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';

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
  deletingAddressId: string | null = null;
  
  constructor(
    private addressService: AddressService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private modalService: NgbModal,
    private toastr: ToastrService
  ) {

  }

  ngOnInit() {
    this.getMyAddressesByUserId();
  }


  getMyAddressesByUserId() {

    let currentUser = localStorage.getItem('currentUser');

    if (currentUser) {
    } else {
      this.router.navigate(['/login']);
    }

    let id = JSON.parse(currentUser).id;
    this.addressService.getAddressByUserId(+id)
      .subscribe(Response => {
        this.address = Response;
        // Sort addresses to show default address first
        this.sortAddresses();
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
    const modalRef = this.modalService.open(DeleteAddressModalContent, { centered: true });
    modalRef.componentInstance.address = address;
    
    modalRef.result.then((result) => {
      if (result === 'confirm') {
        // Show loading state
        this.deletingAddressId = address.id;
        
        this.addressService.deleteAddress(address.id)
          .subscribe(
            data => {
              // Hide loading state
              this.deletingAddressId = null;
              
              // Remove address from local array without reloading
              this.address = this.address.filter(addr => addr.id !== address.id);
              
              // Show success toast
              this.toastr.success('Address deleted successfully!', 'Success');
            },
            error => {
              // Hide loading state
              this.deletingAddressId = null;
              
              console.error('Error deleting address:', error);
              this.toastr.error('Failed to delete address. Please try again.', 'Error');
            }
          );
      }
    }, (reason) => {
      // Modal dismissed
    });
  }

  updateAddress(address: Address) {
    localStorage.removeItem('editAddressId');
    localStorage.setItem('editAddressId', address.id);
    this.router.navigate(['address/edit-address']);
  }

  makeDefaultAddress(address: Address) {
    this.addressService.makeDefaultChange(address)
      .subscribe(Response => {
        // Update the local address list without page reload
        this.address.forEach(addr => {
          addr.isDefault = addr.id === address.id ? 'Y' : 'N';
        });
        // Sort to move default address to first position
        this.sortAddresses();
      });

  }

  sortAddresses() {
    if (this.address && this.address.length > 0) {
      this.address.sort((a, b) => {
        if (a.isDefault === 'Y') return -1;
        if (b.isDefault === 'Y') return 1;
        return 0;
      });
    }
  }

  proceedToCheckout() {
    this.router.navigate(['/billing']);
  }

}

// Delete Confirmation Modal Component
@Component({
  selector: 'delete-address-modal-content',
  template: `
    <div class="modal-header border-0 pb-0">
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss()" [disabled]="isDeleting">
        <i class="zmdi zmdi-close"></i>
      </button>
    </div>
    <div class="modal-body text-center pt-0">
      <div class="mb-3">
        <i class="zmdi zmdi-delete zmdi-hc-5x text-danger"></i>
      </div>
      <h5 class="mb-3">Delete Address?</h5>
      <p class="text-muted mb-4">Are you sure you want to delete this address? This action cannot be undone.</p>
      <div class="d-flex gap-2 justify-content-center">
        <button type="button" class="btn btn-secondary px-4" (click)="activeModal.dismiss()" [disabled]="isDeleting">
          <i class="zmdi zmdi-close mr-1"></i>Cancel
        </button>
        <button type="button" class="btn btn-danger px-4" (click)="confirm()" [disabled]="isDeleting">
          <span *ngIf="!isDeleting">
            <i class="zmdi zmdi-delete mr-1"></i>Delete
          </span>
          <span *ngIf="isDeleting">
            <span class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
            Deleting...
          </span>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .modal-header .close {
      padding: 0;
      margin: 0;
      background: none;
      border: none;
      font-size: 1.5rem;
      opacity: 0.5;
      transition: opacity 0.3s;
    }
    .modal-header .close:hover {
      opacity: 1;
    }
    .modal-header .close i {
      font-size: 1.5rem;
    }
    .btn {
      border-radius: 10px;
      font-weight: 600;
      transition: all 0.3s ease;
    }
    .btn-danger {
      background: linear-gradient(135deg, #FF6B6B 0%, #EE5A6F 100%);
      border: none;
    }
    .btn-danger:hover {
      background: linear-gradient(135deg, #EE5A6F 0%, #FF6B6B 100%);
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(255, 107, 107, 0.4);
    }
    .d-flex.gap-2 {
      gap: 10px;
    }
  `]
})
export class DeleteAddressModalContent {
  address: Address;
  isDeleting: boolean = false;

  constructor(public activeModal: NgbActiveModal) {}

  confirm() {
    this.isDeleting = true;
    this.activeModal.close('confirm');
  }
}
