import { Component, OnInit, DoCheck, HostListener, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';
import { AuthenticationService } from 'src/app/services/auth.service';
import { CanComponentDeactivate } from 'src/app/guards/unsaved-changes.guard';
import { Observable } from 'rxjs';
import { UploadImageComponent } from 'src/app/upload-image/upload-image.component';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit, DoCheck, CanComponentDeactivate {

  convertedImage: any;
  user:any;
  isUserActive: any;
  editForm: FormGroup;
  imageLoaded: boolean = false;

  imageId: number;
  messageToSendP: string = 'PROFILE';
  activeTab: string = 'profile'; // Track active tab

  @ViewChild(UploadImageComponent) uploadImageComponent: UploadImageComponent;

  // Track if form has been modified (dirty) to warn before leaving
  private initialFormValues: any;
  public hasUnsavedChanges: boolean = false;
  public showCancelModal: boolean = false;
  public showSuccessModal: boolean = false;
  private pendingNavigation: any = null;
  private resolveNavigationPromise: (value: boolean) => void = null;

  constructor(
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    this.getProfileView();
  }

  // Lifecycle hook: compute unsaved-changes state by comparing actual values
  ngDoCheck() {
    if (this.editForm && this.initialFormValues) {
      // Compare current form values with initial values
      const currentValues = this.editForm.value;
      this.hasUnsavedChanges = this.hasActualChanges(this.initialFormValues, currentValues);
    }
  }

  // Helper method to check if values actually changed
  private hasActualChanges(initial: any, current: any): boolean {
    // Compare each field value
    for (const key in initial) {
      if (initial.hasOwnProperty(key)) {
        // Convert both to strings and trim for comparison
        const initialValue = (initial[key] || '').toString().trim();
        const currentValue = (current[key] || '').toString().trim();
        
        if (initialValue !== currentValue) {
          return true; // Found a real change
        }
      }
    }
    return false; // No actual changes
  }

  // Warn if user refreshes or closes tab with unsaved changes
  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: BeforeUnloadEvent) {
    if (this.hasUnsavedChanges) {
      $event.preventDefault();
      $event.returnValue = '';
    }
  }


  // getProfileView(): void {
  //   let currentUser = localStorage.getItem('currentUser');
  //   this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
  //     this.user = data;
  //   });
  // }

  getProfileView(): void {
    this.imageLoaded = false;
    let currentUser = localStorage.getItem('currentUser');
   // console.log(JSON.parse(currentUser).id);
    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;
      console.log(this.user);
      if (this.user.active) {
        this.isUserActive = true;
        console.log(this.isUserActive);
      } else {
        this.isUserActive = false;
       // console.log(this.isUserActive);
        console.log("Please complete Profile!");
      }
    });
  }

  onImageLoad() {
    this.imageLoaded = true;
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.editForm.controls['imageId'].setValue(this.imageId);
  }

  onEdit(){
    // Switch to edit tab
    this.activeTab = 'edit';
    
    // Initialize form with empty/default values first
    this.editForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      imageId: ['', Validators.required],
      email: ['', Validators.required],
      contact: ['', Validators.required]
    });

    // Then patch values from user object
    if (this.user) {
      this.editForm.patchValue({
        id: this.user.id || '',
        name: this.user.name || '',
        email: this.user.email || '',
        contact: this.user.contact || ''
      });
      console.log('Form values after patch:', this.editForm.value);
    }

    // Store initial form values and reset dirty state
    this.initialFormValues = this.editForm.value;
    this.editForm.markAsPristine();
    this.hasUnsavedChanges = false;

    // Reset the upload image component
    if (this.uploadImageComponent) {
      this.uploadImageComponent.selectedFile = null;
      this.uploadImageComponent.imgURL = null;
      this.uploadImageComponent.convertedImage = null;
      this.uploadImageComponent.isFileSelected = false;
      this.uploadImageComponent.dispalyPreview = false;
      this.uploadImageComponent.isUploading = false;
      this.uploadImageComponent.imageLoading = false;
      
      // Reset the file input
      const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
      if (fileInput) {
        fileInput.value = '';
      }
    }
  }


  onSubmit() {
   
    this.editForm.controls['id'].setValue(this.user.id);

    console.log(this.editForm.value);
    this.userService.updateUser(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {
          console.log("Profile Updated!");
          
          // Get updated user data from server
          this.userService.getUserById(this.user.id).subscribe(updatedUser => {
            // Update localStorage with new user data
            localStorage.setItem('currentUser', JSON.stringify(updatedUser));
            
            // Notify authentication service to update currentUser observable
            // This will trigger menu to refresh
            this.authenticationService['currentUserSubject'].next(updatedUser);
            
            // Update local user object
            this.user = updatedUser;
            
            // Update initial form values to match saved values
            if (this.editForm) {
              this.initialFormValues = this.editForm.value;
            }
            
            this.editForm.markAsPristine();
            this.hasUnsavedChanges = false; // Reset after successful save
            
            // Show success modal instead of navigating
            this.showSuccessModal = true;
            
            // Switch back to profile tab
            this.goBackToProfile();
          });
        },
        error => {
          alert(error);
        });
  }

  closeSuccessModal() {
    this.showSuccessModal = false;
  }

  onCancel() {
    if (this.hasUnsavedChanges) {
      // Show modal instead of alert
      this.showCancelModal = true;
    } else {
      // No changes, just go back
      this.goBackToProfile();
    }
  }

  confirmDiscard() {
    this.showCancelModal = false;
    this.pendingNavigation = null;
    
    // Resolve navigation promise if it exists (for route guard)
    if (this.resolveNavigationPromise) {
      this.resolveNavigationPromise(true); // Allow navigation
      this.resolveNavigationPromise = null;
    } else {
      // Otherwise, just go back to profile tab (for cancel button)
      this.goBackToProfile();
    }
  }

  cancelDiscard() {
    this.showCancelModal = false;
    this.pendingNavigation = null;
    
    // Resolve navigation promise if it exists (for route guard)
    if (this.resolveNavigationPromise) {
      this.resolveNavigationPromise(false); // Block navigation
      this.resolveNavigationPromise = null;
    }
  }

  private goBackToProfile() {
    // Reset form state
    this.hasUnsavedChanges = false;
    if (this.editForm) {
      this.editForm.markAsPristine();
    }
    
    // Switch back to Profile tab
    this.activeTab = 'profile';
  }

  onProfileTabClick(event: Event) {
    // Check if user has unsaved changes before switching tabs
    if (this.hasUnsavedChanges) {
      event.preventDefault(); // Prevent tab switch
      event.stopPropagation();
      
      // Store the event for later use
      this.pendingNavigation = event;
      
      // Show the custom modal
      this.showCancelModal = true;
      return false;
    }
    
    // Switch to profile tab
    this.activeTab = 'profile';
  }

  get f() { return this.editForm.controls; }

  // CanDeactivate guard implementation
  canDeactivate(): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.hasUnsavedChanges) {
      return true; // No unsaved changes, allow navigation
    }

    // Return a promise that will be resolved when user makes a choice in modal
    return new Promise<boolean>((resolve) => {
      this.resolveNavigationPromise = resolve;
      this.showCancelModal = true;
    });
  }
}
