import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ItemService } from '../../service/item.service';
import { BrandService } from '../../service/brand.service';
import { first } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-item',
  templateUrl: './edit-item.component.html',
  styleUrls: ['./edit-item.component.scss'],
})
export class EditItemComponent implements OnInit {
  brands: any;
  item: any;
  editForm: FormGroup;
  submitted = false;
  imageId: number;
  messageToSendP = 'ITEM';
  mandatoryFields = '*Mandatory fields';
  err;
  capacity = [
    { id: 1, capcityName: '500' },
    { id: 2, capcityName: '1' },
    { id: 3, capcityName: '2' },
    { id: 4, capcityName: '5' },
    { id: 5, capcityName: '20' },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private itemService: ItemService,
    private brandService: BrandService
  ) {}

  ngOnInit() {
    const itemId = localStorage.getItem('editItemId');
    if (!itemId) {
      alert('Invalid action.');
      this.router.navigate(['item/list-item']);
      return;
    }

    this.itemService.getItemById(+itemId).subscribe(
      (data) => {
        this.editForm.controls.id.setValue(data.id);
        this.editForm.controls.brandId.setValue(data.brandId);
        this.editForm.controls.imageId.setValue(data.imageId);
        this.editForm.controls.name.setValue(data.name);
        this.editForm.controls.mrp.setValue(data.mrp);
        this.editForm.controls.capacity.setValue(data.capacity);
        this.editForm.controls.pic.setValue(data.pic);
        this.item = data;
      },
      (error) => {
        this.err = error;
      }
    );

    this.editForm = this.formBuilder.group({
      id: [],
      name: ['', Validators.required],
      capacity: ['', Validators.required],
      brandId: ['', Validators.required],
      //  code: ['', Validators.required],
      // price: ['', Validators.required],
      mrp: ['', Validators.required],
      //  dispPrice: ['', Validators.required],
      //  discount: ['', Validators.required],
      //  type: ['', Validators.required],
      //  description: ['', Validators.required],
      imageId: ['', Validators.required],
      pic: ['', Validators.required],
    });

    this.brandService.getAllBrands().subscribe(
      (data) => {
        this.brands = data;
      },
      (error) => {
        this.err = error;
      }
    );
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.editForm.controls.imageId.setValue(this.imageId);
  }

  get f() {
    return this.editForm.controls;
  }

  /* Select Dropdown error handling */
  public handleError = (controlName: string, errorName: string) => {
    return this.editForm.controls[controlName].hasError(errorName);
  }

  onSubmit() {
    this.submitted = true;
    if (this.editForm.valid) {
      this.itemService
        .updateItem(this.editForm.value)
        .pipe(first())
        .subscribe(
          (data) => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'Item updated',
              showConfirmButton: true,
              timer: 3000,
              timerProgressBar: true,
            });

            this.router.navigate(['item/list-item']);
          },
          (error) => {
            alert(error);
          }
        );
    }
  }
}
