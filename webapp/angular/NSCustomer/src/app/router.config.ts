import { RouterModule, Routes } from '@angular/router';

import { ProductsPage } from './pages/products/products.pages';
import { CheckoutPage } from './pages/checkout/checkout.pages';
import { BillingPage } from './pages/billing/billing.pages';
import { LoginComponent } from './login/login.component';
import { OTPComponent } from './otp/otp.component';
import { AuthGuard } from './_guards';
import { ListAddressDir } from './directives/address/list/listaddressdir.component';
import { MyProfileComponent } from './profile/my-profile/my-profile.component';
import { OrderComponent } from './directives/orders/order.component';
import { AddAddressDir } from './directives/address/add/address.component';
import { UploadImageComponent } from './upload-image/upload-image.component';
import { EditAddressDir } from './directives/address/edit/addressdir';
import { PaymentPage } from './pages/payment/payment.pages';
import { AddressPage } from './pages/address/list/listaddress.pages';
import { AddAddreddPage } from './pages/address/add/add-address.pages';
import { EditAddressPages } from './pages/address/edit/edit-address.pages';


const appRoutes: Routes = [
  { path: 'products', component: ProductsPage },
  { path: 'billing', component: BillingPage, canActivate: [AuthGuard] },
  { path: 'checkout', component: CheckoutPage, canActivate: [AuthGuard] },
  { path: 'payment', component: PaymentPage, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'otp', component: OTPComponent },
  { path: 'address/list', component: ListAddressDir, canActivate: [AuthGuard] },
  { path: 'profile/my-profile', component: MyProfileComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrderComponent, canActivate: [AuthGuard] },
  { path: 'upload-image', component: UploadImageComponent, canActivate: [AuthGuard]},
  { path: 'address/edit-address', component: EditAddressDir, canActivate: [AuthGuard] },
  { path: 'address/add-address', component: AddAddressDir, canActivate: [AuthGuard] },

  {
    path: '',
    redirectTo: '/products',
    pathMatch: 'full'
  }
]

export const RouterConfig = [
  RouterModule.forRoot(
    appRoutes, {
      enableTracing: false,
      useHash: false
    })

];

export const RouterDeclarations = [
  ProductsPage,
  BillingPage,
  CheckoutPage,
  PaymentPage,
  LoginComponent,
  OTPComponent,
  // address pages
  AddressPage,
  AddAddreddPage,
  EditAddressPages
];


