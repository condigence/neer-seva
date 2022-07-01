import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AuthenticationService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
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

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
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
          // console.log(this.returnUrl);
          this.router.navigate(['/']);
       //  document.location.href = '/';

        },
        error => {
          console.log(error);
          this.alertService.error(error);
          this.loading = false;
          this.error = error;
        });


  }
}
