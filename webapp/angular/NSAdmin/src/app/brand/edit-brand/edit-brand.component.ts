import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BrandService } from '../../service/brand.service';
import { first } from 'rxjs/operators';
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';

@Component({
  selector: 'app-edit-brand',
  templateUrl: './edit-brand.component.html',
  styleUrls: ['./edit-brand.component.scss'],
})
export class EditBrandComponent implements OnInit {
  brand: any;
  editForm: FormGroup;
  submitted = false;
  imageId: number;
  messageToSendP = 'BRAND';
  mandatoryFields = '*Mandatory fields';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private brandService: BrandService
  ) {}

  ngOnInit() {
    const id = localStorage.getItem('editBrandId');
    if (!id) {
      alert('Invalid action.');
      this.router.navigate(['item/list-item']);
      return;
    }
    this.editForm = this.formBuilder.group({
      id: [],
      name: ['', Validators.required],
      imageId: ['', Validators.required],
      pic: ['', Validators.required],
      createdByUser:[''],
    });

    console.log(+id);
    this.brandService.getBrandById(+id).subscribe((data) => {
      this.editForm.setValue(data);
      console.log(data);
      this.brand = data;
    });
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.editForm.controls.imageId.setValue(this.imageId);
  }

  get f() {
    return this.editForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.editForm.valid) {
      this.brandService
        .updateBrand(this.editForm.value)
        .pipe(first())
        .subscribe(
          (data) => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'Brand Updated',
              showConfirmButton: true,
              timer: 3000,
              timerProgressBar: true,
            });
            this.router.navigate(['brand/list-brand']);
          },
          (error) => {
            alert(error);
          }
        );
    }
  }
}
