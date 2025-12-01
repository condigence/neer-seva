import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})

export class FilterPipe implements PipeTransform{
  transform(items: any = {}, searchText: string = '') {
    if (!items) {
      return {};
    }

    if (searchText == '') {
      return items;
    }

    return items.filter(item => {
      // Get the item name from nested structure
      const itemName = item.item?.name || item.name || '';
      const search = searchText.toLowerCase();
      return itemName.toLowerCase().includes(search);
    });

  }
}
