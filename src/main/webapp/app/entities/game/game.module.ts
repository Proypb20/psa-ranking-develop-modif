import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { GameComponent } from './game.component';
import { GameDetailComponent } from './game-detail.component';
import { GameUpdateComponent } from './game-update.component';
import { GameDeletePopupComponent, GameDeleteDialogComponent } from './game-delete-dialog.component';
import { gameRoute, gamePopupRoute } from './game.route';

const ENTITY_STATES = [...gameRoute, ...gamePopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [GameComponent, GameDetailComponent, GameUpdateComponent, GameDeleteDialogComponent, GameDeletePopupComponent],
  entryComponents: [GameDeleteDialogComponent]
})
export class PbPointsGameModule {}
