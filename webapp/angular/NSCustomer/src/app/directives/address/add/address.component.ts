import { Component, OnInit, DoCheck, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressService } from 'src/app/services/address.service';
import { CanComponentDeactivate } from 'src/app/guards/unsaved-changes.guard';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'add-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.scss']
})
export class AddAddressDir implements OnInit, DoCheck, CanComponentDeactivate {


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
  
  // Track unsaved changes
  private initialFormValues: any;
  public hasUnsavedChanges: boolean = false;
  private resolveNavigationPromise: (value: boolean) => void = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private addressService: AddressService,
    private modalService: NgbModal) { }
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

    // Store initial form values
    this.initialFormValues = this.addForm.value;
  }

  // Track form changes
  ngDoCheck() {
    if (this.addForm && this.initialFormValues) {
      const currentValues = this.addForm.value;
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

  onSubmit() {    
    let currentUser = localStorage.getItem('currentUser');
    this.addForm.controls['userId'].setValue(JSON.parse(currentUser).id);

    this.submitted = true;
    if (this.addForm.valid) {
      this.addressService.addAddress(this.addForm.value)
        .subscribe(data => {
          // Reset unsaved changes flag on successful save
          this.hasUnsavedChanges = false;
          this.router.navigate(['/']);
        });
    }else{
      console.log("Not valid");
    }
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

  get f() { return this.addForm.controls; }
}

// Separate modal component for unsaved changes
import { Component as ModalComponent, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@ModalComponent({
  selector: 'unsaved-changes-modal',
  template: `
    <div class="modal-header bg-warning text-white">
      <h4 class="modal-title">
        <i class="zmdi zmdi-alert-triangle mr-2"></i>Unsaved Changes
      </h4>
      <button type="button" class="close text-white" aria-label="Close" (click)="activeModal.dismiss('cancel')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body text-center p-4">
      <div class="mb-3">
        <i class="zmdi zmdi-edit zmdi-hc-3x text-warning mb-3"></i>
      </div>
      <h5 class="mb-3">You have unsaved changes</h5>
      <p class="text-muted">Would you like to save your address before leaving this page?</p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-danger" (click)="activeModal.close('leave')">
        <i class="zmdi zmdi-close mr-1"></i>Leave Without Saving
      </button>
      <button type="button" class="btn btn-primary" (click)="activeModal.dismiss('cancel')">
        <i class="zmdi zmdi-edit mr-1"></i>Keep Editing
      </button>
    </div>
  `
})
export class UnsavedChangesModalContent {
  constructor(public activeModal: NgbActiveModal) {}


}
