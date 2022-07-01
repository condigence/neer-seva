import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { VendorComponent } from './vendor/vendor.component';


import { AddStockComponent } from './add-stock/add-stock.component';
import { EditStockComponent } from './edit-stock/edit-stock.component';
import { ListStockComponent } from './list-stock/list-stock.component';

import { ListSaleComponent } from './list-sale/list-sale.component';
import { ListDeliveryComponent } from './list-delivery/list-delivery.component';
import { MyProfileComponent } from './profile/my-profile/my-profile.component';
// import { ListOrderComponent } from './list-order/list-order.component';
import { ListPaymentComponent } from './list-payment/list-payment.component';
// import { OrderDetailComponent } from './order-detail/order-detail.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ImageApprovalComponent } from './image-approval/image-approval.component';
import { AddShopComponent } from './add-shop/add-shop.component';
import { EditShopComponent } from './edit-shop/edit-shop.component';
import { ListShopComponent } from './list-shop/list-shop.component';
// import { ListAddressComponent } from './list-address/list-address.component';
import { EditAddressComponent } from './edit-address/edit-address.component';
import { AddAddressComponent } from './add-address/add-address.component';
import { UploadImageComponent } from './upload-image/upload-image.component';

import { AuthGuard } from './_guards';
import { OTPComponent } from './otp/otp.component';
import { OrderComponent } from './orders/order.component';
import { ModalContentComponent } from './modal-content/modal-content.component';
import { ModalContainerComponent } from './modal-container/modal-container.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [

  {
    path: 'vendor',
    component: VendorComponent, canActivate: [AuthGuard]
  },

  {
    path: 'add-shop',
    component: AddShopComponent, canActivate: [AuthGuard]
  },
  {
    path: 'edit-shop',
    component: EditShopComponent, canActivate: [AuthGuard]
  },
  {
    path: 'list-shop',
    component: ListShopComponent, canActivate: [AuthGuard]
  },
  {
    path: 'add-address',
    component: AddAddressComponent, canActivate: [AuthGuard]
  },
  {
    path: 'edit-address',
    component: EditAddressComponent, canActivate: [AuthGuard]
  },
  // {
  //   path: 'list-address',
  //   component: ListAddressComponent, canActivate: [AuthGuard]
  // },

  {
    path: 'add-stock',
    component: AddStockComponent, canActivate: [AuthGuard]
  },
  {
    path: 'edit-stock',
    component: EditStockComponent, canActivate: [AuthGuard]
  },
  {
    path: 'list-stock',
    component: ListStockComponent, canActivate: [AuthGuard]
  },

  {
    path: 'list-sale',
    component: ListSaleComponent, canActivate: [AuthGuard]
  },
  {
    path: 'list-delivery',
    component: ListDeliveryComponent, canActivate: [AuthGuard]
  },

  {
    path: 'orders/list-order',
    component: OrderComponent, canActivate: [AuthGuard]
  },
  {
    path: 'list-payment',
    component: ListPaymentComponent, canActivate: [AuthGuard]
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

  
  { path: 'profile/my-profile', component: MyProfileComponent, canActivate: [AuthGuard] },




  // {
  //   path: 'profile',
  //   children: [
  //     { 
  //       path: 'my-profile', 
  //       component: MyProfileComponent, 
  //       canActivate: [AuthGuard] }
  //   ]
  // },

  {
    path: '',
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
    path: 'registration',
    component: RegistrationComponent
  },// otherwise redirect to home
  { path: '**', component: NotFoundComponent }



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
