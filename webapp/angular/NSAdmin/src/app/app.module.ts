import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { SidebarComponent } from './sidebar/sidebar.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';

import { ListItemComponent } from './item/list-item/list-item.component';
import { AddUserComponent } from './user/add-user/add-user.component';


import { MyProfileComponent } from './profile/my-profile/my-profile.component';
import { ListOrderComponent } from './list-order/list-order.component';
import { OrderDetailComponent } from './order-detail/order-detail.component';
import { LoginComponent } from './login/login.component';
import { OTPComponent } from './otp/otp.component';
import { RegistrationComponent } from './registration/registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AddBrandComponent } from './brand/add-brand/add-brand.component';
import { EditBrandComponent } from './brand/edit-brand/edit-brand.component';
import { ListBrandComponent } from './brand/list-brand/list-brand.component';
import { ImageApprovalComponent } from './image-approval/image-approval.component';
import { AuthenticationService } from './service/auth.service';
import { AlertService} from './service/alert.service';
import { UserService } from './service/user.service';
import { ProductService } from './service/product.service';
import { CustomerService } from './service/customer.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ItemService } from './service/item.service';
import { BrandService } from './service/brand.service';
import { UploadImageComponent } from './upload-image/upload-image.component';
import { AlertComponent } from './_directives';
import { AuthGuard } from './_guards';
import { ListUserComponent } from './user/list-user/list-user.component';
import { EditUserComponent } from './user/edit-user/edit-user.component';
import { AddItemComponent } from './item/add-item/add-item.component';
import { EditItemComponent } from './item/edit-item/edit-item.component';
import { InterceptorService } from './service/interceptorService';
import { NumberDirective } from './Directives/numberOnlyDirective';
import { PaginationComponent } from './pagination/pagination.component';

// import { JwtInterceptor, ErrorInterceptor } from './_helpers';



@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    AddItemComponent,
    EditItemComponent,
    ListItemComponent,
    AddUserComponent,
    EditUserComponent,
    ListUserComponent,
    MyProfileComponent,
    ListOrderComponent,
    OrderDetailComponent,
    LoginComponent,
    OTPComponent,
    RegistrationComponent,
    ResetPasswordComponent,
    AddBrandComponent,
    EditBrandComponent,
    ListBrandComponent,
    ImageApprovalComponent,
    UploadImageComponent,
    AlertComponent,
    NumberDirective,
    PaginationComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterModule,
    

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true
      },


    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService,
    ProductService,
    CustomerService,
    ItemService,
    BrandService,



      ],
  bootstrap: [AppComponent]
})
export class AppModule { }
