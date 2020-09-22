import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { FormulaComponent } from './formula.component';
import { FormulaDetailComponent } from './formula-detail.component';
import { FormulaUpdateComponent } from './formula-update.component';
import { FormulaDeletePopupComponent, FormulaDeleteDialogComponent } from './formula-delete-dialog.component';
import { formulaRoute, formulaPopupRoute } from './formula.route';

const ENTITY_STATES = [...formulaRoute, ...formulaPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FormulaComponent,
    FormulaDetailComponent,
    FormulaUpdateComponent,
    FormulaDeleteDialogComponent,
    FormulaDeletePopupComponent
  ],
  entryComponents: [FormulaDeleteDialogComponent]
})
export class PbPointsFormulaModule {}
