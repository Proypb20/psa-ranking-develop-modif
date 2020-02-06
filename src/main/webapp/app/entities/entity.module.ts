import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.PsaRankingCountryModule)
      },
      {
        path: 'province',
        loadChildren: () => import('./province/province.module').then(m => m.PsaRankingProvinceModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.PsaRankingLocationModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.PsaRankingCityModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.PsaRankingCityModule)
      },
      {
        path: 'doc-type',
        loadChildren: () => import('./doc-type/doc-type.module').then(m => m.PsaRankingDocTypeModule)
      },
      {
        path: 'tournament',
        loadChildren: () => import('./tournament/tournament.module').then(m => m.PsaRankingTournamentModule)
      },
      {
        path: 'event',
        loadChildren: () => import('./event/event.module').then(m => m.PsaRankingEventModule)
      },
      {
        path: 'team',
        loadChildren: () => import('./team/team.module').then(m => m.PsaRankingTeamModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.PsaRankingCategoryModule)
      },
      {
        path: 'roster',
        loadChildren: () => import('./roster/roster.module').then(m => m.PsaRankingRosterModule)
      },
      {
        path: 'player',
        loadChildren: () => import('./player/player.module').then(m => m.PsaRankingPlayerModule)
      },
      {
        path: 'user-extra',
        loadChildren: () => import('./user-extra/user-extra.module').then(m => m.PsaRankingUserExtraModule)
      },
      {
        path: 'format',
        loadChildren: () => import('./format/format.module').then(m => m.PsaRankingFormatModule)
      },
      {
        path: 'player-point',
        loadChildren: () => import('./player-point/player-point.module').then(m => m.PsaRankingPlayerPointModule)
      },
      {
        path: 'player-detail-point',
        loadChildren: () => import('./player-detail-point/player-detail-point.module').then(m => m.PsaRankingPlayerDetailPointModule)
      },
      {
        path: 'team-point',
        loadChildren: () => import('./team-point/team-point.module').then(m => m.PsaRankingTeamPointModule)
      },
      {
        path: 'team-detail-point',
        loadChildren: () => import('./team-detail-point/team-detail-point.module').then(m => m.PsaRankingTeamDetailPointModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PsaRankingEntityModule {}
