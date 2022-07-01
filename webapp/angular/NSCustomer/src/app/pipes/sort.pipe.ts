import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sortBy'
})

export class SortPipe implements PipeTransform {
  transform(items: any, fieldName: string) {
    let sortFieldSelected = fieldName;
    let sortField = sortFieldSelected.split('|');
   
    items.sort((a: any, b: any) => {
      if (sortField[1] == 'asc' || sortField[1] == 'lth') {
        if (a[sortField[0]] < b[sortField[0]]) {
          return -1;
        } else if (a[sortField[0]] > b[sortField[0]]) {
          return 1;
        } else {
          return 0;
        }
      } else if (sortField[1] == 'desc' || sortField[1] == 'htl') {
        if (a[sortField[0]] > b[sortField[0]]) {
          return -1;
        } else if (a[sortField[0]] < b[sortField[0]]) {
          return 1;
        } else {
          return 0;
        }
      } else {
        if (a[sortField[0]] < b[sortField[0]]) {
          return -1;
        } else if (a[sortField[0]] > b[sortField[0]]) {
          return 1;
        } else {
          return 0;
        }
      }
    });

    /*if(sortField[1] == 'htl'){
      let temp = [];
      for(let i = (items.length -1); i>=0 ; i--){
        temp.push(items[i]);
      }
      items=temp;
    }*/
    return items;

  }


  
}
