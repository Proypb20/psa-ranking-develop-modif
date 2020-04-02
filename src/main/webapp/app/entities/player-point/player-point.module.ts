import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { PlayerPointComponent } from './player-point.component';
import { PlayerPointDetailComponent } from './player-point-detail.component';
import { PlayerPointUpdateComponent } from './player-point-update.component';
import { PlayerPointDeletePopupComponent, PlayerPointDeleteDialogComponent } from './player-point-delete-dialog.component';
import { playerPointRoute, playerPointPopupRoute } from './player-point.route';

const ENTITY_STATES = [...playerPointRoute, ...playerPointPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlayerPointComponent,
    PlayerPointDetailComponent,
    PlayerPointUpdateComponent,
    PlayerPointDeleteDialogComponent,
    PlayerPointDeletePopupComponent
  ],
  entryComponents: [PlayerPointDeleteDialogComponent]
})
export class PbPointsPlayerPointModule {}
