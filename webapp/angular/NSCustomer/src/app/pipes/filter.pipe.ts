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

    return items.filter(items => {
      return items.name.startsWith(searchText.toUpperCase());
    });

  }
}
