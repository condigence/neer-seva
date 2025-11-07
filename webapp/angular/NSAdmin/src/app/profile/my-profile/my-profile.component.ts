import { Component, OnInit } from "@angular/core";
// import { User } from "src/app/model/user.model";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { first } from "rxjs/operators";
// import { UserService } from "src/app/service/user.service";
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';
import { UserService } from "../../service/user.service";
import { User } from "../../model/user.model";

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
  messageToSendP = "PROFILE";

  constructor(
    private userService: UserService,
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
   
    const currentUser = localStorage.getItem("currentUser");
    console.log(currentUser);
    this.userService
      .getUserById(JSON.parse(currentUser).id)
      .subscribe((data: any) => {
        this.user = data;
      });
  }

  receiveMessage($event: number) {
    this.imageId = $event;
    this.editForm.controls["imageId"].setValue(this.imageId);
  }

  get f() {
    return this.editForm.controls;
  }

  onSubmit() {
    console.log(this.editForm.value);
    console.log(this.editForm.valid);
    this.editForm.controls["id"].setValue(this.user.id);
    this.userService
      .updateUser(this.editForm.value)
      .pipe(first())
      .subscribe(
        () => {
          Swal.fire({
            position: "top-end",
            icon: "success",
            title: "You updated your Profile",
            showConfirmButton: true,
            timer: 3000,
          });
          // this.router.navigate(['/profile/my-profile']);
          this.router.navigate(["/"]);
        },
        (error: any) => {
          alert(error);
        }
      );
  }
}
