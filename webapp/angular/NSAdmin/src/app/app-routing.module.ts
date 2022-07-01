import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';


import { AddItemComponent } from './item/add-item/add-item.component';
import { EditItemComponent } from './item/edit-item/edit-item.component';
import { ListItemComponent } from './item/list-item/list-item.component';
import { AddUserComponent } from './user/add-user/add-user.component';
import { EditUserComponent } from './user/edit-user/edit-user.component';
import { ListUserComponent } from './user/list-user/list-user.component';

import { MyProfileComponent } from './profile/my-profile/my-profile.component';
import { ListOrderComponent } from './list-order/list-order.component';
import { OrderDetailComponent } from './order-detail/order-detail.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AddBrandComponent } from './brand/add-brand/add-brand.component';
import { EditBrandComponent } from './brand/edit-brand/edit-brand.component';
import { ListBrandComponent } from './brand/list-brand/list-brand.component';
import { ImageApprovalComponent } from './image-approval/image-approval.component';

import { UploadImageComponent } from './upload-image/upload-image.component';

import { AuthGuard } from './_guards';
import { OTPComponent } from './otp/otp.component';

const routes: Routes = [



  {
    path: 'order-detail',
    component: OrderDetailComponent, canActivate: [AuthGuard]
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent, canActivate: [AuthGuard]
  },

  {
    path: 'image-approval',
    component: ImageApprovalComponent, canActivate: [AuthGuard]
  },
  {
    path: 'upload-image',
    component: UploadImageComponent, canActivate: [AuthGuard]
  },

  {
    path: 'user',
    children: [
      {path: 'add-user', component: AddUserComponent, canActivate: [AuthGuard]},
      {path: 'edit-user', component: EditUserComponent, canActivate: [AuthGuard]},
      {path: 'list-user', component: ListUserComponent, canActivate: [AuthGuard]},
    ]
  },


  {
    path: 'item',
    children: [
      {path: 'add-item', component: AddItemComponent, canActivate: [AuthGuard]},
      {path: 'edit-item', component: EditItemComponent, canActivate: [AuthGuard]},
      {path: 'list-item', component: ListItemComponent, canActivate: [AuthGuard]},
    ]
  },
  {
    path: 'brand',
    children: [
      { path: 'add-brand', component: AddBrandComponent, canActivate: [AuthGuard] },
      { path: 'edit-brand', component: EditBrandComponent, canActivate: [AuthGuard] },
      { path: 'list-brand', component: ListBrandComponent, canActivate: [AuthGuard] },
    ]
  },
  {
    path: 'profile',
    children: [
      { path: 'my-profile', component: MyProfileComponent, canActivate: [AuthGuard] }
    ]
  },

  {
    path: '',
    component: HomeComponent, canActivate: [AuthGuard]
  },
  {
    path: 'home',
    component: HomeComponent, canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'otp',
    component: OTPComponent

  },
  {
    path: 'register',
    component: RegistrationComponent
  }, // otherwise redirect to home
  { path: '**', redirectTo: '' }



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
