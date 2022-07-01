import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Brand } from '../../model/brand.model';
import { BrandService } from '../../service/brand.service';
import Swal from 'sweetalert2';




@Component({
  selector: 'app-list-brand',
  templateUrl: './list-brand.component.html',
  styleUrls: ['./list-brand.component.scss'],
})
export class ListBrandComponent implements OnInit {

  brands: Brand[] = [];
  name: string;

  constructor(
    private router: Router,
    private brandService: BrandService
    ) {}

  ngOnInit() {
    this.getAllBrandsView();
  }

  getAllBrandsView() {
    console.log("Inside get All brands");
    this.brandService.getAllBrands().subscribe((data) => {
      this.brands = data;
      console.log(this.brands);
    });
  }

  // deleteBrand(brand: Brand) {
  //   this.brandService.deleteBrand(brand.id).subscribe((data) => {
  //     this.getAllBrandsView();
  //   });
  // }



  deleteTheBrand(brand: Brand) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this Brand',
      icon: 'warning',
      showCancelButton: true,
      cancelButtonColor: '#3085d6',
      confirmButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.value) {
        this.brandService.deleteBrand(brand.id).subscribe((data) => {
          this.getAllBrandsView();
        });
        Swal.fire(
        'Deleted!',
        `<strong style="color:red;">Your selected Brand has been deleted.</strong>`,
        'success'
      );

      } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire(
        'Cancelled',
        'Your Brand is safe :)',
        'error'
      );
      }
    });
  }




  updateBrand(brand: Brand) {
    localStorage.removeItem('editBrandId');
    localStorage.setItem('editBrandId', brand.id);
    this.router.navigate(['brand/edit-brand']);
  }

  Search() {
    if (this.name !== '') {
    } else if (this.name === '') {
      this.ngOnInit();
    }
    this.brands = this.brands.filter((res) => {
      return res.name.toLowerCase().match(this.name.toLowerCase());
    });
  }


  trackBrand(brand: { id: any }) {
    return brand ? brand.id : undefined;
  }



}
