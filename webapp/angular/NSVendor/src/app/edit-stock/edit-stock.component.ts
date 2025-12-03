import { Component, OnInit } from '@angular/core';
import { StockService } from '../service/stock.service';
import { Router } from '@angular/router';
import { Stock } from '../model/stock.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/auth.service';
import { ShopService } from '../service/shop.service';


@Component({
  selector: 'edit-stock',
  templateUrl: './edit-stock.component.html',
  styleUrls: ['./edit-stock.component.scss']
})
export class EditStockComponent implements OnInit {

  stock: any;
   shop: any;     // <-- store shop info here
  editForm: FormGroup;
  submitted = false;
  currentUser;
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private stockService: StockService,
    private shopService: ShopService,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    // localStorage.getItem('currentUser');
  }

  ngOnInit() {
    const id = localStorage.getItem('editStockId');
    if (!id) {
      alert('Invalid action.');
      this.router.navigate(['list-stock']);
      return;
    }


    this.editForm = this.formBuilder.group({
      id: [],
      quantity: ['', Validators.required],
      itemId: ['', Validators.required],
      shopId: ['', Validators.required],
      userId: [this.currentUser.id, Validators.required]

    });
   // Load stock
    this.stockService.getStockById(+id).subscribe(stockData => {
      this.stock = stockData;
      console.log("Stock:", this.stock);

      // Load shop info (backend does NOT return shop object)
      this.shopService.getShopById(stockData.shopId).subscribe(shopData => {
        this.shop = shopData;
      });

      // Populate form
      this.editForm.patchValue({
        id: stockData.id,
        quantity: stockData.quantity,
        itemId: stockData.item.id,
        shopId: stockData.shopId,
        userId: this.currentUser.id
      });
      });
  }

  // get f() { return this.editForm.controls; }

  // onSubmit() {
  //   this.editForm.controls['id'].setValue(this.stock.id);
  //   this.editForm.controls['shopId'].setValue(this.stock.shop.id);
  //   this.editForm.controls['itemId'].setValue(this.stock.item.id);
  //   this.editForm.controls['userId'].setValue(this.currentUser.id);

  //   //console.log(this.editForm.value);
  //   this.stockService.updateStock(this.editForm.value)
  //     .pipe(first())
  //     .subscribe(
  //       data => {
  //         this.router.navigate(['list-stock']);
  //       },
  //       error => {
  //         console.log(error);
  //       });
  // }

get f() { return this.editForm.controls; }

  onSubmit() {
    this.submitted = true;
    if (this.editForm.invalid) return;

    this.stockService.updateStock(this.editForm.value)
      .pipe(first())
      .subscribe(
        () => this.router.navigate(['list-stock']),
        error => console.log(error)
      );
  }

}
