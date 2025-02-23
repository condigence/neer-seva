import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthenticationService } from "../service/auth.service";
import { AlertService } from "../service/alert.service";
import { first } from "rxjs/operators";
import Swal from "sweetalert2";
// import { MyValidationService } from "src/app/service/myValidatorService";

@Component({
  selector: "app-registration",
  templateUrl: "./registration.component.html",
  styleUrls: ["./registration.component.scss"],
})
export class RegistrationComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  submitted = false;
  returnUrl!: string;
  userExist!: false;
  error!: string;
  mandatoryFields = "*Mandatory fields";

  constructor(
    private formBuilder: FormBuilder,   
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService    
  ) {
    // redirect to home if already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      name: ["", Validators.required],
      contact: [
        "",
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
        ],
        // ValidateContactNotTaken.createValidator(this.validatorService)
      ],
      email: [""],
      type: ["ADMIN"],
    });

    // get return url from route parameters or default to '/'
    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.registerForm.controls;
  }

  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.registerForm.controls[controlName].hasError(errorName);
  };

  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService
      .verifyRegistration(this.registerForm.value)
      .pipe(first())
      .subscribe(
        (data) => {
          this.router.navigate(["/otp"], {
            queryParams: { registered: false },
          });
        },
        (error) => {
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "User alredy registered!",
            footer: `<strong>Please try with different contact number</strong>`,
          });
          //  this.router.navigate(['/register'], { queryParams: { registered: false } });
          this.alertService.error(error.errorMessage);
          this.error = error;
          this.loading = false;
        }
      );
  }
}
