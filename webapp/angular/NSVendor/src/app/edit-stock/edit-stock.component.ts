import { Component, OnInit } from '@angular/core';
import { StockService } from '../service/stock.service';
import { Router } from '@angular/router';
import { Stock } from '../model/stock.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/auth.service';


@Component({
  selector: 'edit-stock',
  templateUrl: './edit-stock.component.html',
  styleUrls: ['./edit-stock.component.scss']
})
export class EditStockComponent implements OnInit {

  stock: any;
  editForm: FormGroup;
  submitted = false;
  currentUser;
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private stockService: StockService,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    localStorage.getItem('currentUser');
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
    this.stockService.getStockById(+id)
      .subscribe(data => {

        this.stock = data;



        // this.editForm.setValue(data);


      });
  }

  get f() { return this.editForm.controls; }

  onSubmit() {
    this.editForm.controls['id'].setValue(this.stock.id);
    this.editForm.controls['shopId'].setValue(this.stock.shop.id);
    this.editForm.controls['itemId'].setValue(this.stock.item.id);
    this.editForm.controls['userId'].setValue(this.currentUser.id);

    //console.log(this.editForm.value);
    this.stockService.updateStock(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['list-stock']);
        },
        error => {
          console.log(error);
        });
  }



}
