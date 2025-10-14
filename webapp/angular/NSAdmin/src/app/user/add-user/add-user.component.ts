import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { MyValidationService } from 'src/app/service/myValidatorService';
import { ValidateContactNotTaken } from 'src/app/validator/contact.validator';



@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss'],
})
export class AddUserComponent implements OnInit {
  addForm: FormGroup;
  submitted = false;
  message: string;
  error: any;
  imageId: number;
  messageToSendP = 'PROFILE';
  mandatoryFields = '* Mandatory fields';

  userType = [
    { id: 2, name: 'CUSTOMER' },
    { id: 3, name: 'VENDOR' },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private validatorService: MyValidationService
  ) {}

  ngOnInit() {
    this.addForm = this.formBuilder.group({
      name: ['', Validators.required],
      contact: [
        '',
         [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10)
        ],
         // ValidateContactNotTaken.createValidator(this.validatorService)
      ],
     // email: ['', [Validators.required, Validators.email]],
      email: [''],
      type: [''],
      imageId: [''],
    });





  }


  receiveMessage($event) {
    this.imageId = $event;
    this.addForm.controls.imageId.setValue(this.imageId);
  }

  onSubmit() {
    console.log(this.addForm.value);
    console.log(this.addForm.valid);
    this.submitted = true;
    if (this.addForm.invalid) {
      return;
    }
    if (this.addForm.valid) {
      this.userService.addUser(this.addForm.value).subscribe(
        (data) => {
          console.log(data);
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'User has been added',
            text: 'Successfully',
            showConfirmButton: true,
            timer: 3000,
            timerProgressBar: true,
          });
          this.router.navigate(['user/list-user'], {
            queryParams: { added: true },
          });
        },
        (error) => {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'User alredy registered!',
            footer: `<strong>Please try with different contact number</strong>`
          });

          // this.alertService.error(error.errorMessage);
          // this.error = error;
          // this.loading = false;
        }
      );
    }
  }

  get f() {
    return this.addForm.controls;
  }

  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.addForm.controls[controlName].hasError(errorName);
  }

  changeUserType(e: any) {
    this.addForm.get('type').setValue(e.target.value, {
      onlySelf: true,
    });
  }




}
