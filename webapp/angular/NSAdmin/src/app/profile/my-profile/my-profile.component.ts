import { Component, OnInit } from "@angular/core";
// import { User } from "src/app/model/user.model";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { first } from "rxjs/operators";
// import { UserService } from "src/app/service/user.service";
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';
import { UserService } from "../../service/user.service";
import { User } from "../../model/user.model";
import { AuthenticationService } from "src/app/service/auth.service";

@Component({
  selector: "app-my-profile",
  templateUrl: "./my-profile.component.html",
  styleUrls: ["./my-profile.component.scss"],
})
export class MyProfileComponent implements OnInit {
  convertedImage: any;
  isUserActive: any;
  user: User = new User;
  editForm!: FormGroup;
  imageId!: number;
  imageChanged = false;

  messageToSendP = "PROFILE";
  // whether a file has been selected in the child upload component
  fileChosen: boolean = false;
  // validation error shown when Update is clicked but chosen file not uploaded
  uploadValidationError: string | null = null;

  constructor(
    private userService: UserService,
    private authService: AuthenticationService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.editForm = this.formBuilder.group({
      id: [],
      name: ["", Validators.required],
      imageId: [""],
      email: ["", Validators.required],
      contact: ["", Validators.required],
    });

    this.getProfileView();
  }

  getProfileView(): void {
   
    // const currentUser = localStorage.getItem("currentUser");
    // console.log(currentUser);
    // this.userService
    //   .getUserById(JSON.parse(currentUser).id)
    //   .subscribe((data: any) => {
    //     this.user = data;
    //   });

  const currentUser = this.authService.currentUserValue;

  this.userService.getUserById(currentUser.id).subscribe(data => {
    this.user = data;
    this.editForm.patchValue(data);  // populate the form
  });

  }

  receiveMessage($event: number) {
    this.imageId = $event;
    this.editForm.controls["imageId"].setValue(this.imageId);

    this.imageChanged = true;       // <-- important
  this.editForm.controls["imageId"].markAsDirty();  // <-- also important

    // upload completed
    this.fileChosen = false;
    this.uploadValidationError = null;
  }

  onFileSelected(flag: boolean) {
    this.fileChosen = flag;
    if (!flag) {
      this.uploadValidationError = null;
    }
  }

  //a method to determine whether to disable the Update button
  get disableUpdateButton(): boolean {
  const formChanged = this.editForm.dirty;
  const imageUpdated = this.imageChanged;

  const noChanges = !formChanged && !imageUpdated;
  return this.editForm.invalid || noChanges;
}


  get f() {
    return this.editForm.controls;
  }

  // onSubmit() {
  //   console.log(this.editForm.value);
  //   console.log(this.editForm.valid);
  //   // Require an uploaded image before allowing update (per UX requirement)
  //   if (!this.editForm.controls['imageId'].value) {
  //     this.uploadValidationError = 'Please choose and upload a profile image before updating.';
  //     return;
  //   }
  //   this.editForm.controls["id"].setValue(this.user.id);
  //   this.userService
  //     .updateUser(this.editForm.value)
  //     .pipe(first())
  //     .subscribe(
  //       () => {
  //         Swal.fire({
  //           position: "top-end",
  //           icon: "success",
  //           title: "You updated your Profile",
  //           showConfirmButton: true,
  //           timer: 3000,
  //         });
  //         // this.router.navigate(['/profile/my-profile']);
  //         this.router.navigate(["/"]);
  //       },
  //       (error: any) => {
  //         alert(error);
  //       }
  //     );
  // }


  onSubmit() {
  if (!this.editForm.controls['imageId'].value) {
    this.uploadValidationError = 'Please choose and upload a profile image before updating.';
    return;
  }

  this.editForm.controls["id"].setValue(this.user.id);

  this.userService.updateUser(this.editForm.value)
    .pipe(first())
    .subscribe(() => {

  const loggedInUser = this.authService.currentUserValue;

  // Fetch full updated user from backend
  this.userService.getUserById(loggedInUser.id).subscribe(fullUser => {

    // Update localStorage + header
    this.authService.setCurrentUser(fullUser);

    // Update profile page local user
    this.user = fullUser;

    Swal.fire({
      position: "top-end",
      icon: "success",
      title: "You updated your Profile",
      showConfirmButton: true,
      timer: 3000,
    });

    this.router.navigate(["/"]);
  });

});

}

}
