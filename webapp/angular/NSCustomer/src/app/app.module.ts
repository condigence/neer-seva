import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';

import { HelloComponent } from './hello.component';
import { Pipe, PipeTransform } from '@angular/core';
import { dirConfig } from './dir.config';
import { RouterConfig, RouterDeclarations } from './router.config';
import { ProductsModel } from './model/products.model';
import { BillingFormModel } from './model/billingformfields.model';
import { CompanyDetailsModel } from './model/companydetails.model';
import { FilterPipe } from './pipes/filter.pipe';
import { SortPipe } from './pipes/sort.pipe';
import { StorageService } from './services/storage.service';
import { CartService } from './services/cart.service';
import { ItemService } from './services/item.service';
import { HttpClientModule } from '@angular/common/http';
import { AlertService } from './services/alert.service';
import { AlertComponent } from './_directives';
import { AddressService } from './services/address.service';
import { ProfileService } from './services/profile.service';
import { MyProfileComponent } from './profile/my-profile/my-profile.component';
import { OrderService } from './services/order.service';
import { OrderComponent } from './directives/orders/order.component';
import { UserService } from './services/user.service';
import { UploadImageComponent } from './upload-image/upload-image.component';


//import { OwlCarouselDemoComponent } from './components/owl-carousel-demo/owl-carousel-demo.component';
import { CarasualComponent } from './carasual/carasual.component';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from './modal/modal.component';
import { ModalModule, ModalService } from './modal';
@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CarouselModule,
    ModalModule,
    NgbModule,
    RouterConfig],

  declarations: [
    AppComponent,
    HelloComponent,
    AlertComponent,
    MyProfileComponent,
    CarasualComponent,
    dirConfig,
    RouterDeclarations,
    FilterPipe,
    UploadImageComponent,
    OrderComponent,
    SortPipe
  ],
  providers: [
    ProductsModel,
    BillingFormModel,
    CompanyDetailsModel,
    StorageService,
    CartService,
    ItemService,
    AlertService,
    UserService,
    OrderService,
    ModalService,
    AddressService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
