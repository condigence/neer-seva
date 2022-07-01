import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthenticationService } from 'src/app/service/auth.service';

@Component({
  selector: 'list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.scss'],
})
export class ListUserComponent implements OnInit {

  users: User[] = [];
  res: User[] = [];
  name: string;
  currentUser: any;
  items = [];
  pageOfItems: Array<any>;

  constructor(
    private router: Router,
    private userService: UserService,
    private authenticationService: AuthenticationService
    ) {}

  ngOnInit() {
    this.getAllUsers();    
    this.currentUser =   JSON.parse(localStorage.getItem('currentUser'));   
  }

  getAllUsers() {    
    this.userService.getAllUsers().subscribe((data) => {
      this.users = data;    
      console.log(this.users);
      let combined = this.users.filter(object => (object.type == 'VENDOR' || object.type == 'CUSTOMER'));
      this.res = [...combined];         
    });    
  }

  addUser(): void {
    this.router.navigate(['user/add-user']);
  }

  
  updateUser(user: User) {    
    localStorage.removeItem('editUserId');
    localStorage.setItem('editUserId', user.id);
    this.router.navigate(['user/edit-user']);
  }

  Search() {
    if (this.name !== '') {
    } else if (this.name === '') {
      this.ngOnInit();
    }
    this.res = this.res.filter((res) => {
      return res.name.toLowerCase().match(this.name.toLowerCase());
    });
  }

  trackUser(user: { id: any }) {
    return user ? user.id : undefined;
  }

  onChangePage(pageOfItems: Array<any>) {    
    this.pageOfItems = pageOfItems;    
  }




  deleteTheUser(user: User) {
    const currentUser = this.authenticationService.currentUserValue;
    if ((user.type == 'ADMIN') && (user.contact === currentUser.contact)) {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You are admin and logged in cant delete!',
      });
    }
    if (user.type == 'ADMIN') {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You can not delete an admin!',
      });
    }

    if (user.contact === currentUser.contact) {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You are logged In Can not delete!',
      });
    }

    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this user',
      icon: 'warning',
      showCancelButton: true,
      cancelButtonColor: '#3085d6',
      confirmButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.value) {
        this.userService.deleteUser(user.id).subscribe((data) => {
          this.getAllUsers();
        });
        Swal.fire(
        'Deleted!',
        `<strong style="color:red;">Your selected User has been deleted.</strong>`,
        'success'
      );

      } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire(
        'Cancelled',
        'Your selected has been safe :)',
        'error'
      );
      }
    });
  }








}
