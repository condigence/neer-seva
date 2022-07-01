import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { VendorComponent } from './vendor/vendor.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';


import { ListStockComponent } from './list-stock/list-stock.component';
import { EditStockComponent } from './edit-stock/edit-stock.component';
import { AddStockComponent } from './add-stock/add-stock.component';


import { SalesComponent } from './sales/sales.component';
import { DeliveryComponent } from './delivery/delivery.component';
import { ListSaleComponent } from './list-sale/list-sale.component';
import { ListDeliveryComponent } from './list-delivery/list-delivery.component';
import { MyProfileComponent } from './profile/my-profile/my-profile.component';
// import { ListOrderComponent } from './list-order/list-order.component';
import { ListPaymentComponent } from './list-payment/list-payment.component';
// import { OrderDetailComponent } from './order-detail/order-detail.component';
import { LoginComponent } from './login/login.component';
import { OTPComponent } from './otp/otp.component';
import { RegistrationComponent } from './registration/registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

import { ImageApprovalComponent } from './image-approval/image-approval.component';
import { AuthenticationService } from './service/auth.service';
import { AlertService} from './service/alert.service';
import { UserService } from './service/user.service';
import { VendorService } from './service/vendor.service';
import { ProductService } from './service/product.service';
import { StockService } from './service/stock.service';
import { CustomerService } from './service/customer.service';
import { ItemService } from './service/item.service';
import { BrandService } from './service/brand.service';

import { AddShopComponent } from './add-shop/add-shop.component';
import { ListShopComponent } from './list-shop/list-shop.component';
import { EditShopComponent } from './edit-shop/edit-shop.component';
import { ShopService } from './service/shop.service';
import { ListAddressComponent } from './list-address/list-address.component';
import { AddAddressComponent } from './add-address/add-address.component';
import { EditAddressComponent } from './edit-address/edit-address.component';
import { AddressService } from './service/address.service';
import { UploadImageComponent } from './upload-image/upload-image.component';
import { ConfirmationPopoverModule } from 'angular-confirmation-popover';


import { AlertComponent } from './_directives';
import { AuthGuard } from './_guards';


import { ProfileService } from './service/profile.service';
import { OrderComponent } from './orders/order.component';
import { OrderService } from './service/order.service';
import { ModalService } from './service/modal.service';
import { ModalModule } from './modal';
import { ModalContainerComponent } from './modal-container/modal-container.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NotFoundComponent } from './not-found/not-found.component';
import { NumberDirective } from './_directives/numberOnlyDirective';




@NgModule({
  declarations: [
    
    AppComponent,
    VendorComponent,
    SidebarComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    ListStockComponent,
    EditStockComponent,
    AddStockComponent,
    SalesComponent,
    DeliveryComponent,
    ListSaleComponent,
    ListDeliveryComponent,
    MyProfileComponent,
    OrderComponent,
    ListPaymentComponent,
    // OrderDetailComponent,
    LoginComponent,
    OTPComponent,
    RegistrationComponent,
    ResetPasswordComponent,
    ImageApprovalComponent,
    AddShopComponent,
    ListShopComponent,
    EditShopComponent,
    ListAddressComponent,
    AddAddressComponent,
    EditAddressComponent,
    UploadImageComponent,
    AlertComponent,
    ModalContainerComponent,
    NotFoundComponent,
    NumberDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ModalModule,
    NgbModule,
    ReactiveFormsModule,
    ConfirmationPopoverModule.forRoot({
      confirmButtonType: 'danger'
    })

  ],
  providers: [

    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService,
    VendorService,
    ProductService,
    OrderService,
    StockService,
    CustomerService,
    ShopService,
    BrandService,
    ItemService,
    AddressService,
    ModalService,
    ProfileService
      ],
  bootstrap: [AppComponent]
})
export class AppModule { }
