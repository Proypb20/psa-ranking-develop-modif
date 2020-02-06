import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { TeamPointComponent } from './team-point.component';
import { TeamPointDetailComponent } from './team-point-detail.component';
import { TeamPointUpdateComponent } from './team-point-update.component';
import { TeamPointDeletePopupComponent, TeamPointDeleteDialogComponent } from './team-point-delete-dialog.component';
import { teamPointRoute, teamPointPopupRoute } from './team-point.route';

const ENTITY_STATES = [...teamPointRoute, ...teamPointPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TeamPointComponent,
    TeamPointDetailComponent,
    TeamPointUpdateComponent,
    TeamPointDeleteDialogComponent,
    TeamPointDeletePopupComponent
  ],
  entryComponents: [TeamPointDeleteDialogComponent]
})
export class PsaRankingTeamPointModule {}
