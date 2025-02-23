import { Component, OnInit } from '@angular/core';
import { Item } from '../../model/item.model';
import { Router } from '@angular/router';
import { ItemService } from '../../service/item.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.scss'],
})
export class ListItemComponent implements OnInit {
  public popoverTitle = 'Are You Sure to Delete??';
  public popoverMessage = `<strong>This will delete parmanently</strong>`;
  public confirmClicked = false;
  public cancelClicked = false;

  items: Item[] = [];
  name!: string;
  constructor(private router: Router, private itemService: ItemService) {}

  ngOnInit() {
    this.getAllItems();
  }

  getAllItems() {
    this.itemService.getAllItems().subscribe((data) => {
      this.items = data;
    });
  }

  addItem(): void {
    this.router.navigate(['item/add-item']);
  }

  // deleteItem(item: Item) {
  //   this.itemService.deleteItem(item.id).subscribe((data) => {
  //     this.getAllItems();
  //   });
  // }



  deleteTheItem(item: Item) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this item',
      icon: 'warning',
      showCancelButton: true,
      cancelButtonColor: '#3085d6',
      confirmButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.value) {
        this.itemService.deleteItem(item.id).subscribe((data) => {
          this.getAllItems();
        });
        Swal.fire(
        'Deleted!',
        `<strong style="color:red;">Your selected Item has been deleted.</strong>`,
        'success'
      );

      } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire(
        'Cancelled',
        'Your item is safe :)',
        'error'
      );
      }
    });
  }

  updateItem(item: Item) {
    localStorage.removeItem('editItemId');
    localStorage.setItem('editItemId', item.id);
    this.router.navigate(['item/edit-item']);
  }

  Search() {
    if (this.name !== '') {
    } else if (this.name === '') {
      this.ngOnInit();
    }
    this.items = this.items.filter((res) => {
      return res.name.toLowerCase().match(this.name.toLowerCase());
    });
  }

  trackUser(item: { id: any }) {
    return item ? item.id : undefined;
  }
}
