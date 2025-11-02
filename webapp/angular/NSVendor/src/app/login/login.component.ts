import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { first } from "rxjs/operators";
import { AuthenticationService } from "../service/auth.service";
import { AlertService } from "../service/alert.service";
import Swal from "sweetalert2";
import { UserService } from "../service/user.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  userExist: boolean = false;
  error: string;
  users: any = [];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      contact: [
        "",
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
        ],
      ],
    });

    this.userService.getAllUsers().subscribe((user) => (this.users = user));

    // get return url from route parameters or default to '/'
    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    let userInput = this.loginForm.value.contact;
    // Guard against users not yet loaded
    if (!this.users || !Array.isArray(this.users) || this.users.length === 0) {
      Swal.fire({
        icon: "warning",
        title: "Please wait...",
        text: "User data is still loading. Try again in a moment.",
      });
      return;
    }

    let store = this.users.filter((value) => value.contact == userInput);
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
      if (store[0].type === "VENDOR") {
        this.submitted = true;
        this.loading = true;
        this.authenticationService
          .verifyLogin(this.loginForm.value)
          .pipe(first())
          .subscribe((data) => {
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
          });        
      } else {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          width: 600,
          padding: "3em",
          text: `Your are not Registered As a Vendor!`,
          footer: `<strong style="color:red;">Please Contact to Neerseva Service!</strong>`,
        });
      }
    }
  }



}
