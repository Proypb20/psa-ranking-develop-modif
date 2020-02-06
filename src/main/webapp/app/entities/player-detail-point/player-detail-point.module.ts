import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { PlayerDetailPointComponent } from './player-detail-point.component';
import { PlayerDetailPointDetailComponent } from './player-detail-point-detail.component';
import { PlayerDetailPointUpdateComponent } from './player-detail-point-update.component';
import {
  PlayerDetailPointDeletePopupComponent,
  PlayerDetailPointDeleteDialogComponent
} from './player-detail-point-delete-dialog.component';
import { playerDetailPointRoute, playerDetailPointPopupRoute } from './player-detail-point.route';

const ENTITY_STATES = [...playerDetailPointRoute, ...playerDetailPointPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlayerDetailPointComponent,
    PlayerDetailPointDetailComponent,
    PlayerDetailPointUpdateComponent,
    PlayerDetailPointDeleteDialogComponent,
    PlayerDetailPointDeletePopupComponent
  ],
  entryComponents: [PlayerDetailPointDeleteDialogComponent]
})
export class PsaRankingPlayerDetailPointModule {}
