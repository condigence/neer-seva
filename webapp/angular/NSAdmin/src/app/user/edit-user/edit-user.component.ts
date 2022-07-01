import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss'],
})
export class EditUserComponent implements OnInit {
  user: User;
  editForm: FormGroup;
  submitted = false;
  imageId: number;
  messageToSendP = 'PROFILE';
  mandatoryFields = '*Mandatory fields';

  userType = [
    { id: 2, name: 'CUSTOMER' },
    { id: 3, name: 'VENDOR' },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit() {
    const id = localStorage.getItem('editUserId');
    console.log(id);
    if (!id) {
      alert('Invalid action');
      this.router.navigate(['user/list-user']);
      return;
    }
    this.editForm = this.formBuilder.group({
      id: [],
      name: ['', Validators.required],
      type: ['', Validators.required],
      contact: ['', Validators.required],
      // email: ["", [Validators.required, Validators.email]],
      email: ['', Validators.email],
      imageId: [''],
      pic: [''],
    });
    this.userService.getUserById(+id).subscribe((data) => {
      // Set for all
      //  this.editForm.setValue(data[0]);

     // this.editForm.get('type').setValue(data.type);

      console.log(data);
      //  set individual
      this.editForm.setValue({
        id: data.id,
        name: data.name,
        contact: data.contact,
        email: data.email,
        pic: data.pic,
        type: data.type,
        imageId: data.imageId,
      });
      this.user = data;
    });
  }

  receiveMessage($event) {
    this.imageId = $event;

    this.editForm.controls.imageId.setValue(this.imageId);
  }

  get f() {
    return this.editForm.controls;
  }

  public handleError = (controlName: string, errorName: string) => {
    return this.editForm.controls[controlName].hasError(errorName);
  }

  changeType(e) {
    this.editForm.get('type').setValue(e.target.value, {
      onlySelf: true,
    });

    console.log(e.target.value);
  //   this.editForm.get("type").valueChanges
  //   .subscribe(f=> {
  //     this.onTypeChanged(f);
  // })

  }



  onSubmit() {
    this.submitted = true;
    console.log(this.editForm.value);

    if (this.editForm.valid) {
      this.userService
        .updateUser(this.editForm.value)
        .pipe(first())
        .subscribe(
          (data) => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'User Updated',
              showConfirmButton: true,
              timer: 3000,
              timerProgressBar: true,
            });
            this.router.navigate(['user/list-user']);
          },
          (error) => {
            alert(error);
          }
        );
    }
  }
}
