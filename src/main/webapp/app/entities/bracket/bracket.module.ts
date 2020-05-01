import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { BracketComponent } from './bracket.component';
import { BracketDetailComponent } from './bracket-detail.component';
import { bracketRoute, bracketPopupRoute } from './bracket.route';

const ENTITY_STATES = [...bracketRoute, ...bracketPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [BracketComponent, BracketDetailComponent],
  entryComponents: []
})
export class PbPointsBracketModule {}
