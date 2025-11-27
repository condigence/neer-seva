import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { AuthenticationService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
import { ToastService } from '../services/toast.service';
@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.scss']
})
export class OTPComponent implements OnInit {
  otpForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  userExist: false;
  error: string;
  showErrorModal: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private modalService: NgbModal,
    private toastService: ToastService
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.currentUserValue) {

      console.log(this.authenticationService.currentUserValue);
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.otpForm = this.formBuilder.group({
      otp: ['', Validators.required],
      contact: ['']
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.otpForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.otpForm.invalid) {
      return;
    }

    let contact = localStorage.getItem('userContact');
    //console.log("contact from local storage : " + contact);

   
    //console.log("contact from local storage : " + contact);
    this.otpForm.controls['contact'].setValue(contact);
    this.loading = true;
    this.authenticationService.verifyOTP(this.otpForm.value)
      .pipe(first())
      .subscribe(
        data => {
          console.log('OTP verified successfully', data);
          // Check if user is_active to determine redirect
          if (data && data.active === true) {
            // Active user - redirect to products page
            this.router.navigate(['/products'], { queryParams: { justLoggedIn: 'true' } });
          } else {
            // Inactive user - redirect to edit profile page in edit mode
            this.router.navigate(['/profile/my-profile'], { queryParams: { editMode: 'true' } });
          }
        },
        error => {
          console.log(error);
          this.loading = false;
          this.error = error;
          this.showErrorModal = true;
        });


  }

  closeErrorModal() {
    this.showErrorModal = false;
  }
}
