import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { TeamDetailPointComponent } from './team-detail-point.component';
import { TeamDetailPointDetailComponent } from './team-detail-point-detail.component';
import { TeamDetailPointUpdateComponent } from './team-detail-point-update.component';
import { TeamDetailPointDeletePopupComponent, TeamDetailPointDeleteDialogComponent } from './team-detail-point-delete-dialog.component';
import { teamDetailPointRoute, teamDetailPointPopupRoute } from './team-detail-point.route';

const ENTITY_STATES = [...teamDetailPointRoute, ...teamDetailPointPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TeamDetailPointComponent,
    TeamDetailPointDetailComponent,
    TeamDetailPointUpdateComponent,
    TeamDetailPointDeleteDialogComponent,
    TeamDetailPointDeletePopupComponent
  ],
  entryComponents: [TeamDetailPointDeleteDialogComponent]
})
export class PbPointsTeamDetailPointModule {}
