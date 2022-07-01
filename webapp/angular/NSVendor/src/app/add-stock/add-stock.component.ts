import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StockService } from '../service/stock.service';
import { Router } from '@angular/router';
import { BrandService } from '../service/brand.service';
import { ItemService } from '../service/item.service';
import { AuthenticationService } from '../service/auth.service';
import { ShopService } from '../service/shop.service';
import { UserService } from '../service/user.service';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-add-stock',
  templateUrl: './add-stock.component.html',
  styleUrls: ['./add-stock.component.scss']
})
export class AddStockComponent implements OnInit {
  items: {};
  brands: {};
  name;
  addForm: FormGroup;
  submitted = false;
  currentUser;
  profile;
  shops;
  mandatoryFields = '*Mandatory fields';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private stockService: StockService,
    private shopService: ShopService,
    private brandService: BrandService,
    private itemService: ItemService,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
      localStorage.getItem('currentUser');
      this.userService.getUserById(this.currentUser.id).subscribe(data => {
        this.profile = data;
      });
   }


  ngOnInit() {
    this.addForm = this.formBuilder.group({
      quantity: ['', Validators.required],
      itemId: ['', Validators.required],
      shopId: ['', Validators.required],
      userId: [this.currentUser.id, Validators.required]
    });
   
    this.getShopByUserId(this.currentUser.id);
    this.getAllBrand();
  }


  onChangeBrand(id) {    
    if (id) { 
      this.itemService.getItemsByBrandId(id)
        .subscribe(
          data => {
            this.items = data;
          }
        );
    } else {
      this.items = null;
    }
  }

  getAllBrand() {
    this.brandService.getAllBrands()
      .subscribe(data => {
        this.brands = data;       
      });
  }


  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.addForm.controls[controlName].hasError(errorName);
  }

  changeItem(e: { target: { value: any; }; }) {
    this.addForm.get('itemId').setValue(e.target.value, {
      onlySelf: true,
    });
  }
  changeShop(e: { target: { value: any; }; }) {
    this.addForm.get('shopId').setValue(e.target.value, {
      onlySelf: true,
    });
  }

  onSubmit() { 
    this.submitted = true;
    if (this.addForm.valid) {
      this.stockService.addStock(this.addForm.value)
        .subscribe(data => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Stock has been added',
            text: 'Successfully',
            showConfirmButton: true,
            timer: 3000,
            timerProgressBar: true,
          });
          this.router.navigate(['list-stock']);
        });
    }
  }


  getShopByUserId(id) {
    //console.log(id);
    this.shopService.getShopByVendorId(id)
    .subscribe(Response => {
      this.shops = Response; 
      //console.log(this.shops); 

    });
  }


  get f() { return this.addForm.controls; }
}
