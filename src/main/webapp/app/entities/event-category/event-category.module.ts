import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { EventCategoryComponent } from './event-category.component';
import { EventCategoryDetailComponent } from './event-category-detail.component';
import { EventCategoryUpdateComponent } from './event-category-update.component';
import { EventCategoryDeletePopupComponent, EventCategoryDeleteDialogComponent } from './event-category-delete-dialog.component';
import { eventCategoryRoute, eventCategoryPopupRoute } from './event-category.route';

const ENTITY_STATES = [...eventCategoryRoute, ...eventCategoryPopupRoute];

@NgModule({
  imports: [PbPointsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EventCategoryComponent,
    EventCategoryDetailComponent,
    EventCategoryUpdateComponent,
    EventCategoryDeleteDialogComponent,
    EventCategoryDeletePopupComponent
  ],
  entryComponents: [EventCategoryDeleteDialogComponent]
})
export class PbPointsEventCategoryModule {}
