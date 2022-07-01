import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AuthenticationService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
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
            this.router.navigate(['/']);
        }
       
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            contact: ['', Validators.required]
           //, Validators.pattern(/^[6-9]\d{9}$/)
        });

        // get return url from route parameters or default to '/'
        //this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    onSubmit() {

        this.submitted = true;

      //  console.log("Submitting!"+this.f.contact.value);

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;

        
        this.authenticationService.verifyLogin(this.loginForm.value)
            .pipe(first())
            .subscribe(
                data => {
                   // console.log("Registered User!");
                    //this.router.navigate(['otp']);
                    this.router.navigate(['/otp'], { queryParams: { registered: true } });

                },
                error => {
                    console.log(error);
                    this.alertService.error(error);
                    this.loading = false;
                    this.error = error;
                });


    }
}
