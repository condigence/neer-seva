import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthenticationService } from '../../service/auth.service';

@Component({
  selector: 'list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.scss'],
})
export class ListUserComponent implements OnInit {

  users: User[] = [];
  res: User[] = [];
  name: string = '';
  currentUser: any;
  items = [];
  // pagination removed for now; keep simple res list

  constructor(
    private router: Router,
    private userService: UserService,
    private authenticationService: AuthenticationService
  ) {}



  ngOnInit() {
    this.getAllUsers();
    this.currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe(
      (data) => {
        // Resolve common envelope shapes: array, { data: [] }, { users: [] }, { results: [] }
        let usersArray: any[] = [];
        if (Array.isArray(data)) {
          usersArray = data;
        } else if (data && Array.isArray((data as any).data)) {
          usersArray = (data as any).data;
        } else if (data && Array.isArray((data as any).users)) {
          usersArray = (data as any).users;
        } else if (data && Array.isArray((data as any).results)) {
          usersArray = (data as any).results;
        } else if (data && typeof data === 'object') {
          // fallback: pick the first array-valued property if any
          for (const k of Object.keys(data)) {
            if (Array.isArray((data as any)[k])) {
              usersArray = (data as any)[k];
              break;
            }
          }
        }

        this.users = usersArray || [];
        // For now, show all users (don't filter by type) so the list is visible in UI.
        this.res = [...this.users];
        console.log('users loaded into res, length=', this.res.length);
      },
      (err) => {
        this.users = [];
        this.res = [];
      }
    );
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
      this.res = this.res.filter((res) => {
        return res.name.toLowerCase().match(this.name.toLowerCase());
      });
    } else {
      this.ngOnInit();
    }
  }

  trackUser(index: number, user: { id: any }) {
    return user && user.id ? user.id : index;
  }

  // image error handler used by template
  onImgError(event: any) {
    try { (event.target as HTMLImageElement).src = 'assets/images/df_user.png'; } catch (e) { }
  }

  deleteTheUser(user: User) {
    const currentUser = this.authenticationService.currentUserValue;
    if ((user.type == 'ADMIN') && (user.contact === currentUser.contact)) {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You are admin and logged in, can\'t delete!',
      });
    }
    if (user.type == 'ADMIN') {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You cannot delete an admin!',
      });
    }

    if (user.contact === currentUser.contact) {
      return Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'You are logged in, cannot delete!',
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
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.userService.deleteUser(user.id).subscribe(() => {
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
          'Your selected user is safe :)',
          'error'
        );
      }
    });
  }
}