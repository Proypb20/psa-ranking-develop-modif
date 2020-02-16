import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterByTournament'
  })

export class TournamentFilterPipe implements PipeTransform {
    transform(items: Array<any>, Id: number): Array<any> {
        if(!items) return [];
        if(!Id) return items;
        return items.filter(item => item.tournamentId === Id);
    }
}