import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/auth.service';
import { AlertService } from '../service/alert.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.scss']
})
export class OTPComponent implements OnInit {
  otpForm: FormGroup;  
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
      this.router.navigate(['/']);
    }
  }




  ngOnInit() {
    this.otpForm = this.formBuilder.group({
      otp: ['', Validators.required],
      contact: ['']
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.otpForm.controls; }

  onSubmit() {
    this.submitted = true;
    if (this.otpForm.invalid) {
      return;
    }
    const contact = localStorage.getItem('userContact');
    this.otpForm.controls.contact.setValue(contact);    
    if (this.route.snapshot.queryParamMap.get('registered') == 'true') {
      this.authenticationService.verifyOTP(this.otpForm.value)
        .pipe(first())
        .subscribe(
          data => {
            if (data.active == true) {
              Swal.fire({
                icon: 'success',
                width: 600,
                padding: '3em',
                title: 'Your are Logged in!',
                text: 'Welcome to NeerSeva!',
                timer: 5000,
                timerProgressBar: true,
                footer: `<strong style="color:purple;">Be Yourself Be Pure!</strong>`,
                backdrop: `
                  rgba(0,0,123,0.4)
                  no-repeat
                `
              });
              this.router.navigate(['']);
            } else {
              Swal.fire({
                icon: 'success',
                width: 600,
                padding: '3em',
                title: 'Your are Logged in!',
                text: 'Welcome to NeerSeva!',
                timer: 5000,
                timerProgressBar: true,
                footer: `<strong style="color:purple;">Be Yourself Be Pure!</strong>`,
                backdrop: `
                  rgba(0,0,123,0.4)
                  left top
                  no-repeat
                `
              });
              this.router.navigate(['profile/my-profile']);
            }
          },
          error => {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              width: 500,
              padding: '1em',
              text: `You entered a wrong OTP!`,
              footer: `<strong style="color:red;">Please try with a Valid OTP!</strong>`
            });
            this.alertService.error(error);            
            this.error = error;
          });

    } else {

      if (this.otpForm.get('otp').value == '1234') {
        const newuser = JSON.parse(localStorage.getItem('newUser'));
        this.authenticationService.register(newuser)
          .pipe(first())
          .subscribe(
            data => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'You are Registered',
                text: 'You are Redirected to login plzz try to login',
                showConfirmButton: true,
                timer: 5000,
                timerProgressBar: true,
              });
              this.router.navigate(['/']);
              this.router.navigate(['login']);
            },
            error => {

              //  this.router.navigate(['/register'], { queryParams: { registered: false } });
              this.alertService.error(error.errorMessage);
              this.error = error;              
            });

      } else {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          width: 500,
          padding: '1em',
          text: `You entered a wrong OTP!`,
          footer: `<strong style="color:red;">Please try with a Valid OTP!</strong>`
        });
        // console.log(error);
        //     this.alertService.error(error);
        //     this.loading = false;
        //     this.error = error;
      }

    }



  }
}
