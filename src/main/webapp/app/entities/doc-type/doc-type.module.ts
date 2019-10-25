import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { DocTypeComponent } from './doc-type.component';
import { DocTypeDetailComponent } from './doc-type-detail.component';
import { DocTypeUpdateComponent } from './doc-type-update.component';
import { DocTypeDeletePopupComponent, DocTypeDeleteDialogComponent } from './doc-type-delete-dialog.component';
import { docTypeRoute, docTypePopupRoute } from './doc-type.route';

const ENTITY_STATES = [...docTypeRoute, ...docTypePopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DocTypeComponent,
    DocTypeDetailComponent,
    DocTypeUpdateComponent,
    DocTypeDeleteDialogComponent,
    DocTypeDeletePopupComponent
  ],
  entryComponents: [DocTypeDeleteDialogComponent]
})
export class PsaRankingDocTypeModule {}
