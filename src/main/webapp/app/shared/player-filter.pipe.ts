import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterByPlayerProfile'
  })

export class PlayerFilterPipe implements PipeTransform {
    transform(items: Array<any>, profileName: any): Array<any> {
        if(!items) return [];
        if(!profileName) return items;
        return items.filter(item => item.profile === profileName);
    }
}