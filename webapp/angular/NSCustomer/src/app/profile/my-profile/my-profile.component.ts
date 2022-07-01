import { Component, OnInit } from '@angular/core';
import { HttpClient } from 'selenium-webdriver/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit {

  convertedImage: any;
  user:any;
  isUserActive: any;
  editForm: FormGroup;

  imageId: number;
  messageToSendP: string = 'PROFILE';

  constructor(
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    this.getProfileView();

    // this.editForm = this.formBuilder.group({
    //   id: [],
    //   name: [this.user.name, Validators.required],
    //   imageId: ['', Validators.required],
    //   email: [this.user.email, Validators.required],
    //   contact: [this.user.contact, Validators.required]
    // });

    
  }


  // getProfileView(): void {
  //   let currentUser = localStorage.getItem('currentUser');
  //   this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
  //     this.user = data;
  //   });
  // }

  getProfileView(): void {
   
    let currentUser = localStorage.getItem('currentUser');
   // console.log(JSON.parse(currentUser).id);
    this.userService.getUserById(JSON.parse(currentUser).id).subscribe(data => {
      this.user = data;
      console.log(this.user);
      if (this.user.active) {
        this.isUserActive = true;
        console.log(this.isUserActive);
      } else {
        this.isUserActive = false;
       // console.log(this.isUserActive);
        console.log("Please complete Profile!");
      }
    });
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.editForm.controls['imageId'].setValue(this.imageId);
  }

  onEdit(){
    this.editForm = this.formBuilder.group({
      id: [],
      name: [this.user.name, Validators.required],
      imageId: ['', Validators.required],
      email: [this.user.email, Validators.required],
      contact: [this.user.contact, Validators.required]
    });

  }


  onSubmit() {
   
    this.editForm.controls['id'].setValue(this.user.id);

    console.log(this.editForm.value);
    this.userService.updateUser(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {
          console.log("Profile Updated!");
          //this.router.navigate(['/profile/my-profile']);
          this.router.navigate(['/']);
          // document.location.href = '/'; 
        },
        error => {
          alert(error);
        });
  }

  get f() { return this.editForm.controls; }
}
