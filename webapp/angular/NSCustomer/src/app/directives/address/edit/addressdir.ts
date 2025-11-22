import { Component, DoCheck, HostListener } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AddressService } from 'src/app/services/address.service';
import { first } from 'rxjs/operators';
import { CanComponentDeactivate } from 'src/app/guards/unsaved-changes.guard';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UnsavedChangesModalContent } from '../add/address.component';


@Component({
  selector: 'edit-address',
  templateUrl: './addressdir.html',
  styleUrls: ['./addressdir.scss']
})


export class EditAddressDir implements DoCheck, CanComponentDeactivate {
  editForm: FormGroup;
  submitted = false;
  addressId: string;
  
  // Track unsaved changes
  private initialFormValues: any;
  public hasUnsavedChanges: boolean = false;
  private resolveNavigationPromise: (value: boolean) => void = null;

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
    private modalService: NgbModal) {}

   ngOnInit() {
    const id = localStorage.getItem('editAddressId');
    console.log(id);
    if (!id) {
      alert('Invalid action.');
      this.router.navigate(['address/list']);
      return;
    }
    
    this.addressId = id;
    
    this.editForm = this.formBuilder.group({
      id: [''],
      type:  ['', Validators.required],
      line1: ['', Validators.required],
      line2: ['', Validators.required],
      line3: ['', Validators.required],
      line4: ['', Validators.required],
      pin:   ['', Validators.required],
      city:  ['', Validators.required],
      state: ['', Validators.required],
      country:  ['', Validators.required],
      userId:  [''],
      isDefault: ['']
    });
    
    // Load existing address data
    this.addressService.getAddressbyId(+id)
      .subscribe(data => {
        if (data) {
          this.editForm.patchValue(data);
          // Store initial form values after loading
          setTimeout(() => {
            this.initialFormValues = this.editForm.value;
          }, 100);
        }
      }, error => {
        console.error('Error loading address:', error);
      });
  }



  // Track form changes
  ngDoCheck() {
    if (this.editForm && this.initialFormValues) {
      const currentValues = this.editForm.value;
      this.hasUnsavedChanges = this.hasActualChanges(this.initialFormValues, currentValues);
    }
  }

  // Check if form values actually changed
  private hasActualChanges(initial: any, current: any): boolean {
    for (const key in initial) {
      if (initial.hasOwnProperty(key)) {
        const initialValue = (initial[key] || '').toString().trim();
        const currentValue = (current[key] || '').toString().trim();
        if (initialValue !== currentValue) {
          return true;
        }
      }
    }
    return false;
  }

  // Warn on browser refresh/close
  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: BeforeUnloadEvent) {
    if (this.hasUnsavedChanges) {
      $event.preventDefault();
      $event.returnValue = '';
    }
  }

  // Navigation guard method
  canDeactivate(): Promise<boolean> | boolean {
    if (!this.hasUnsavedChanges) {
      return true;
    }

    return new Promise<boolean>((resolve) => {
      this.resolveNavigationPromise = resolve;
      this.openUnsavedChangesModal();
    });
  }

  // Open modal for unsaved changes
  openUnsavedChangesModal() {
    const modalRef = this.modalService.open(UnsavedChangesModalContent, {
      centered: true,
      size: 'md',
      backdrop: 'static'
    });

    modalRef.result.then(
      (result) => {
        if (result === 'leave') {
          this.hasUnsavedChanges = false;
          if (this.resolveNavigationPromise) {
            this.resolveNavigationPromise(true);
            this.resolveNavigationPromise = null;
          }
        }
      },
      (reason) => {
        // Modal dismissed (stay on page)
        if (this.resolveNavigationPromise) {
          this.resolveNavigationPromise(false);
          this.resolveNavigationPromise = null;
        }
      }
    );
  }

  get f() { return this.editForm.controls; }

  onSubmit() {
    this.submitted = true;
    console.log(this.editForm.value);
    
    if (this.editForm.valid) {
      this.addressService.updateAddress(this.editForm.value)
        .pipe(first())
        .subscribe(
          data => {
            // Reset unsaved changes flag and update initial values
            this.hasUnsavedChanges = false;
            this.initialFormValues = this.editForm.value;
            
            // Show success modal
            this.showSuccessModal();
          },
          error => {
            alert('Error updating address: ' + error);
          });
    } else {
      console.log('Form is invalid');
    }
  }

  showSuccessModal() {
    const modalRef = this.modalService.open(UpdateSuccessModalContent, { centered: true });
    
    modalRef.result.then(() => {
      this.router.navigate(['/address/list']);
    }, () => {
      this.router.navigate(['/address/list']);
    });
  }
}

// Success Modal Component
@Component({
  selector: 'update-success-modal',
  template: `
    <div class="modal-header border-0 pb-0">
      <button type="button" class="close" aria-label="Close" (click)="activeModal.close()">
        <i class="zmdi zmdi-close"></i>
      </button>
    </div>
    <div class="modal-body text-center pt-0">
      <div class="mb-3">
        <i class="zmdi zmdi-check-circle zmdi-hc-5x text-success"></i>
      </div>
      <h5 class="mb-3">Address Updated Successfully!</h5>
      <p class="text-muted mb-4">Your address has been updated successfully.</p>
      <button type="button" class="btn btn-success px-4" (click)="activeModal.close()">
        <i class="zmdi zmdi-check mr-1"></i>OK
      </button>
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
    .btn-success {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      border-radius: 10px;
      font-weight: 600;
    }
    .btn-success:hover {
      background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
    }
  `]
})
export class UpdateSuccessModalContent {
  constructor(public activeModal: NgbActiveModal) {}
}




