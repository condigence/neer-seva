import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/auth.service';
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  submitted = false;
  returnUrl = '/';
  userExist: boolean = false;
  error: string = '';
  users: any[] = [];

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
    // this.userService.getAllUsers().subscribe({
    //   next: (user) => (this.users = user || []),
    //   error: (err) => {
    //     console.error('Failed to load users', err);
    //     // ensure users is an array to avoid runtime errors
    //     this.users = [];
    //   },
    // });
    // get return url from route parameters or default to '/'
  // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
   
    // mark form as submitted for validation UI
    this.submitted = true;

    // stop if form is invalid
    if (this.loginForm.invalid) {
      Swal.fire({
        icon: 'error',
        title: 'Invalid input',
        text: 'Please enter a valid 10-digit contact number.',
      });
      return;
    }

    const userInput = (this.loginForm.get('contact')?.value || '').toString().trim();
    console.log('Login attempt for contact:', userInput);

    // Guard: ensure users is an array before calling filter
    const usersArray = Array.isArray(this.users) ? this.users : [];

    // If users list hasn't loaded or is empty, fallback to server-side verification
    if (usersArray.length === 0) {
      // proceed to server verification and let server decide
      this.authenticationService
        .verifyLogin(this.loginForm.value)
        .pipe(first())
        .subscribe({
          next: (data) => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'Your are Redirected to otp page',
              text: 'Proceed for OTP!',
              showConfirmButton: true,
              timerProgressBar: true,
              timer: 5000,
              footer: `<strong style="color:purple;">Go for OTP!</strong>`,
            });
            this.router.navigate(['/otp'], { queryParams: { registered: true } });
          },
          error: (err) => {
            console.error('Login error:', err);
            // If backend returns 404 for contact not found, prefer that message
            let message = 'An error occurred. Please try again later.';
            if (err?.status === 0) {
              message = 'Network Error! Please contact Admin!';
            } else if (err?.status === 404) {
              message = err?.error?.errorMessage || 'Contact Not Found! Please Register';
            } else if (err?.error?.message) {
              message = err.error.message;
            }
            // Save structured error so other components can inspect if needed
            this.error = JSON.stringify({ errorMessage: message });
            Swal.fire({ icon: 'error', title: 'Login failed', text: message });
          },
        });
      return;
    }

    const store = usersArray.filter((value: { contact: any }) => value.contact == userInput);

    if (store.length === 0) {
      const msg = 'Contact Not Found! Please Register';
      this.error = JSON.stringify({ errorMessage: msg });
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        width: 600,
        padding: '3em',
        text: msg,
        footer: `<strong style="color:red;">Please Try to Registered!</strong>`,
      });
      return;
    }

    const matchedUser = store[0];
    if (matchedUser?.type !== 'ADMIN') {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        width: 600,
        padding: '3em',
        text: `Your are not Registered As a Admin!`,
        footer: `<strong style="color:red;">Please Contact to Neerseva Service!</strong>`,
      });
      return;
    }

    // proceed with server verification for ADMIN users
    this.authenticationService
      .verifyLogin(this.loginForm.value)
      .pipe(first())
      .subscribe({
        next: (data) => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Your are Redirected to otp page',
            text: 'Proceed for OTP!',
            showConfirmButton: true,
            timerProgressBar: true,
            timer: 5000,
            footer: `<strong style="color:purple;">Go for OTP!</strong>`,
          });
          this.router.navigate(['/otp'], { queryParams: { registered: true } });
        },
        error: (err) => {
          console.error('Login error:', err);
          // Prefer structured backend errorMessage when contact not found (404)
          let message = 'An error occurred. Please try again later.';
          if (err?.status === 0) {
            message = 'Network Error! Please contact Admin!';
          } else if (err?.status === 404) {
            message = err?.error?.errorMessage || 'Contact Not Found! Please Register';
          } else if (err?.error?.message) {
            message = err.error.message;
          }
          this.error = JSON.stringify({ errorMessage: message });
          Swal.fire({ icon: 'error', title: 'Login failed', text: message });
        },
      });
  }




}
