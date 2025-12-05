import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sortBy'
})

export class SortPipe implements PipeTransform {
  transform(items: any, fieldName: string) {
    if (!items || !fieldName) {
      return items;
    }
    
    let sortFieldSelected = fieldName;
    let sortField = sortFieldSelected.split('|');
   
    items.sort((a: any, b: any) => {
      // Handle nested item structure (a.item.name, a.item.price)
      let aVal = a.item && a.item[sortField[0]] !== undefined ? a.item[sortField[0]] : a[sortField[0]];
      let bVal = b.item && b.item[sortField[0]] !== undefined ? b.item[sortField[0]] : b[sortField[0]];
      
      // Convert to lowercase for string comparison
      if (typeof aVal === 'string') aVal = aVal.toLowerCase();
      if (typeof bVal === 'string') bVal = bVal.toLowerCase();
      
      if (sortField[1] == 'asc' || sortField[1] == 'lth') {
        if (aVal < bVal) {
          return -1;
        } else if (aVal > bVal) {
          return 1;
        } else {
          return 0;
        }
      } else if (sortField[1] == 'desc' || sortField[1] == 'htl') {
        if (aVal > bVal) {
          return -1;
        } else if (aVal < bVal) {
          return 1;
        } else {
          return 0;
        }
      } else {
        if (aVal < bVal) {
          return -1;
        } else if (aVal > bVal) {
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
