import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { PersonComponent } from './person.component';
import { PersonDetailComponent } from './person-detail.component';
import { PersonUpdateComponent } from './person-update.component';
import { PersonDeletePopupComponent, PersonDeleteDialogComponent } from './person-delete-dialog.component';
import { personRoute, personPopupRoute } from './person.route';

const ENTITY_STATES = [...personRoute, ...personPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PersonComponent, PersonDetailComponent, PersonUpdateComponent, PersonDeleteDialogComponent, PersonDeletePopupComponent],
  entryComponents: [PersonDeleteDialogComponent]
})
export class PsaRankingPersonModule {}
