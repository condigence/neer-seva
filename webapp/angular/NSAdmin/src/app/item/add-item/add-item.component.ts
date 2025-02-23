import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ItemService } from '../../service/item.service';
import { BrandService } from '../../service/brand.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-item',
  templateUrl: './add-item.component.html',
  styleUrls: ['./add-item.component.scss'],
})
export class AddItemComponent implements OnInit {
  brands: any;
  addForm!: FormGroup;
  submitted = false;
  imageId!: number;
  messageToSendP = 'ITEM';
  mandatoryFields = '*Mandatory fields';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private itemService: ItemService,
    private brandService: BrandService
  ) {}

  capacity = [
    { id: 1, capcityName: '500' },
    { id: 2, capcityName: '1' },
    { id: 3, capcityName: '2' },
    { id: 4, capcityName: '5' },
    { id: 5, capcityName: '20' },
  ];

  ngOnInit() {
    this.addForm = this.formBuilder.group({
      name: ['', Validators.required],
      capacity: ['', Validators.required],
      brandId: ['', Validators.required],
      // code: ['', Validators.required],
      //  price: ['', Validators.required],
      mrp: ['', Validators.required],
      //  dispPrice: ['', Validators.required],
      //  discount: ['', Validators.required],
      //  type: ['', Validators.required],
      //  description: ['', Validators.required],
      //  tax: ['', Validators.required],
      imageId: ['', Validators.required],
    });
    this.brandService.getAllBrands().subscribe((data) => {
      this.brands = data;
    });
  }

  receiveMessage($event: number) {
    this.imageId = $event;
    this.addForm.controls['imageId'].setValue(this.imageId);
  }
  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.addForm.controls[controlName].hasError(errorName);
  }

  // changeCapacity(e) {
  //   this.addForm.get("capacity").setValue(e.target.value, {
  //     onlySelf: true,
  //   });
  // }

  // changeBrand(e) {
  //   this.addForm.get("name").setValue(e.target.value, {
  //     onlySelf: true,
  //   });
  // }

  get f() {
    return this.addForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.addForm.valid) {
      this.itemService.addItem(this.addForm.value).subscribe((data) => {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Item added',
          text: 'Successfully',
          showConfirmButton: true,
          timer: 3000,
          timerProgressBar: true,
        });
        this.router.navigate(['item/list-item']);
      });
    } else {
      console.log('form not valid!');
    }
  }
}
