import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.PbPointsCountryModule)
      },
      {
        path: 'province',
        loadChildren: () => import('./province/province.module').then(m => m.PbPointsProvinceModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.PbPointsLocationModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.PbPointsCityModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.PbPointsCityModule)
      },
      {
        path: 'doc-type',
        loadChildren: () => import('./doc-type/doc-type.module').then(m => m.PbPointsDocTypeModule)
      },
      {
        path: 'tournament',
        loadChildren: () => import('./tournament/tournament.module').then(m => m.PbPointsTournamentModule)
      },
      {
        path: 'event',
        loadChildren: () => import('./event/event.module').then(m => m.PbPointsEventModule)
      },
      {
        path: 'team',
        loadChildren: () => import('./team/team.module').then(m => m.PbPointsTeamModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.PbPointsCategoryModule)
      },
      {
        path: 'roster',
        loadChildren: () => import('./roster/roster.module').then(m => m.PbPointsRosterModule)
      },
      {
        path: 'player',
        loadChildren: () => import('./player/player.module').then(m => m.PbPointsPlayerModule)
      },
      {
        path: 'user-extra',
        loadChildren: () => import('./user-extra/user-extra.module').then(m => m.PbPointsUserExtraModule)
      },
      {
        path: 'format',
        loadChildren: () => import('./format/format.module').then(m => m.PbPointsFormatModule)
      },
      {
        path: 'player-point',
        loadChildren: () => import('./player-point/player-point.module').then(m => m.PbPointsPlayerPointModule)
      },
      {
        path: 'player-detail-point',
        loadChildren: () => import('./player-detail-point/player-detail-point.module').then(m => m.PbPointsPlayerDetailPointModule)
      },
      {
        path: 'team-point',
        loadChildren: () => import('./team-point/team-point.module').then(m => m.PbPointsTeamPointModule)
      },
      {
        path: 'team-detail-point',
        loadChildren: () => import('./team-detail-point/team-detail-point.module').then(m => m.PbPointsTeamDetailPointModule)
      },
      {
        path: 'event-category',
        loadChildren: () => import('./event-category/event-category.module').then(m => m.PbPointsEventCategoryModule)
      },
      {
        path: 'game',
        loadChildren: () => import('./game/game.module').then(m => m.PbPointsGameModule)
      },
      {
        path: 'bracket',
        loadChildren: () => import('./bracket/bracket.module').then(m => m.PbPointsBracketModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PbPointsEntityModule {}
