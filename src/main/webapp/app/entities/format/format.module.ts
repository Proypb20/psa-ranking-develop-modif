import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { FormatComponent } from './format.component';
import { FormatDetailComponent } from './format-detail.component';
import { FormatUpdateComponent } from './format-update.component';
import { FormatDeletePopupComponent, FormatDeleteDialogComponent } from './format-delete-dialog.component';
import { formatRoute, formatPopupRoute } from './format.route';

const ENTITY_STATES = [...formatRoute, ...formatPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [FormatComponent, FormatDetailComponent, FormatUpdateComponent, FormatDeleteDialogComponent, FormatDeletePopupComponent],
  entryComponents: [FormatDeleteDialogComponent]
})
export class PbPointsFormatModule {}
