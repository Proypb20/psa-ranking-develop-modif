import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { UserExtraComponent } from './user-extra.component';
import { UserExtraDetailComponent } from './user-extra-detail.component';
import { UserExtraUpdateComponent } from './user-extra-update.component';
import { UserExtraDeletePopupComponent, UserExtraDeleteDialogComponent } from './user-extra-delete-dialog.component';
import { userExtraRoute, userExtraPopupRoute } from './user-extra.route';

const ENTITY_STATES = [...userExtraRoute, ...userExtraPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserExtraComponent,
    UserExtraDetailComponent,
    UserExtraUpdateComponent,
    UserExtraDeleteDialogComponent,
    UserExtraDeletePopupComponent
  ],
  entryComponents: [UserExtraDeleteDialogComponent]
})
export class PsaRankingUserExtraModule {}
