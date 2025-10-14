import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/auth.service';
import { AlertService } from '../service/alert.service';
import Swal from 'sweetalert2';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup; 
  submitted = false;
  returnUrl!: string;
  userExist!: false;
  error!: string;
  users: any = [];

  constructor(
    private formBuilder: FormBuilder,    
    private router: Router,
    private authenticationService: AuthenticationService,    
    private userService: UserService
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group(
      {
        contact: [
          '',
           [
             Validators.required,
            Validators.minLength(10),
            Validators.maxLength(10),
           ],
        ],
      }

    );
    this.userService.getAllUsers().subscribe({
      next: (user) => (this.users = user || []),
      error: (err) => {
        console.error('Failed to load users', err);
        // ensure users is an array to avoid runtime errors
        this.users = [];
      },
    });
    // get return url from route parameters or default to '/'
    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
   
    let userInput = this.loginForm.value.contact;
    console.log(userInput);
    // Guard: ensure users is an array before calling filter
    const usersArray = Array.isArray(this.users) ? this.users : [];
    let store = usersArray.filter((value: { contact: any; }) => value.contact == userInput);    
    if (store.length === 0) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        width: 600,
        padding: "3em",
        text: `Your are not Registered!`,
        footer: `<strong style="color:red;">Please Try to Registered!</strong>`,
      });
    }
    if (store.length > 0) {
      if (store[0].type === "ADMIN") {
        this.submitted = true;        
        this.authenticationService
          .verifyLogin(this.loginForm.value)
          .pipe(first())
          .subscribe({
            next: (data) => {
              Swal.fire({
                position: "center",
                icon: "success",
                title: "Your are Redirected to otp page",
                text: "Proceed for OTP!",
                showConfirmButton: true,
                timerProgressBar: true,
                timer: 5000,
                footer: `<strong style="color:purple;">Go for OTP!</strong>`,
              });
              this.router.navigate(["/otp"], {
                queryParams: { registered: true },
              });
            },
            error: (err) => {
              console.error('Login error:', err);
              // If network error or server unavailable, HttpErrorResponse will be present
              let message = 'An error occurred. Please try again later.';
              if (err?.status === 0) {
                // A client-side or network error occurred. Status 0 indicates network failure
                message = 'Network Error! Please contact Admin!';
              } else if (err?.error?.message) {
                message = err.error.message;
              }
              Swal.fire({
                icon: 'error',
                title: 'Login failed',
                text: message,
              });
            }
          });        
      } else {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          width: 600,
          padding: "3em",
          text: `Your are not Registered As a Admin!`,
          footer: `<strong style="color:red;">Please Contact to Neerseva Service!</strong>`,
        });
      }
    }
  }




}
