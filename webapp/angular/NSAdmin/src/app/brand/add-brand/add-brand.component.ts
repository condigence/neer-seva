import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { BrandService } from '../../service/brand.service';
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';

@Component({
  selector: 'app-add-brand',
  templateUrl: './add-brand.component.html',
  styleUrls: ['./add-brand.component.scss'],
})
export class AddBrandComponent implements OnInit {
  message: string;
  addForm: FormGroup;
  submitted = false;
  mandatoryFields = '*Mandatory fields';
  imageId: number;
  messageToSendP = 'BRAND';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private brandService: BrandService
  ) {}

  ngOnInit() {
    this.addForm = this.formBuilder.group({
      name: ['', Validators.required],
      imageId: ['', Validators.required],
      createdByUserId: ['', Validators.required],
    });
  }

  receiveMessage($event) {
    this.imageId = $event;
    this.addForm.controls.imageId.setValue(this.imageId);
  }

  onSubmit() {
    const currentUserId = JSON.parse(localStorage.getItem('currentUser')).id;
    this.addForm.controls.createdByUserId.setValue(currentUserId);
    this.submitted = true;
    Object.keys(this.addForm.controls).forEach((key) => {
      this.addForm.get(key).markAsDirty();
    });

    if (this.addForm.valid) {
      this.brandService.addBrand(this.addForm.value).subscribe((data) => {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Brand added',
          text: 'Successfully',
          showConfirmButton: true,
          timer: 3000,
          timerProgressBar: true,
        });
  this.router.navigate(['/brand', 'list-brand']);
      });
    }
  }

  get f() {
    return this.addForm.controls;
  }
}
