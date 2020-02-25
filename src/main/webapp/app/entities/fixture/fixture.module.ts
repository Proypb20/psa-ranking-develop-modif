import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { FixtureComponent } from './fixture.component';
import { FixtureDetailComponent } from './fixture-detail.component';
import { FixtureUpdateComponent } from './fixture-update.component';
import { FixtureDeletePopupComponent, FixtureDeleteDialogComponent } from './fixture-delete-dialog.component';
import { fixtureRoute, fixturePopupRoute } from './fixture.route';

const ENTITY_STATES = [...fixtureRoute, ...fixturePopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FixtureComponent,
    FixtureDetailComponent,
    FixtureUpdateComponent,
    FixtureDeleteDialogComponent,
    FixtureDeletePopupComponent
  ],
  entryComponents: [FixtureDeleteDialogComponent]
})
export class PsaRankingFixtureModule {}
