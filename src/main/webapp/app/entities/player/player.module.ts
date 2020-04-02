import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { PlayerComponent } from './player.component';
import { PlayerDetailComponent } from './player-detail.component';
import { PlayerUpdateComponent } from './player-update.component';
import { PlayerDeletePopupComponent, PlayerDeleteDialogComponent } from './player-delete-dialog.component';
import { playerRoute, playerPopupRoute } from './player.route';
import { PlayerFilterPipe } from 'app/shared/player-filter.pipe';

const ENTITY_STATES = [...playerRoute, ...playerPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PlayerComponent, PlayerDetailComponent, PlayerUpdateComponent, PlayerDeleteDialogComponent, PlayerDeletePopupComponent,PlayerFilterPipe],
  entryComponents: [PlayerDeleteDialogComponent],
  exports: [PlayerComponent, PlayerUpdateComponent]
})
export class PbPointsPlayerModule {}
