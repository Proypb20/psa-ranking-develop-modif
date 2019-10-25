import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { RosterComponent } from './roster.component';
import { RosterDetailComponent } from './roster-detail.component';
import { RosterUpdateComponent } from './roster-update.component';
import { RosterDeletePopupComponent, RosterDeleteDialogComponent } from './roster-delete-dialog.component';
import { rosterRoute, rosterPopupRoute } from './roster.route';

const ENTITY_STATES = [...rosterRoute, ...rosterPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [RosterComponent, RosterDetailComponent, RosterUpdateComponent, RosterDeleteDialogComponent, RosterDeletePopupComponent],
  entryComponents: [RosterDeleteDialogComponent]
})
export class PsaRankingRosterModule {}
