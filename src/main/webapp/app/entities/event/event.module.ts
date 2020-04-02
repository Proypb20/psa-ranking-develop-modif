import { NgModule } from '@angular/core';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { EventComponent } from './event.component';
import { EventDetailComponent } from './event-detail.component';
import { EventUpdateComponent } from './event-update.component';
import { EventDeletePopupComponent, EventDeleteDialogComponent } from './event-delete-dialog.component';
import { EvRoute } from './event.route';
import { PbPointsEventCategoryModule } from 'app/entities/event-category/event-category.module';

@NgModule({
  imports: [EvRoute,PbPointsEventCategoryModule,PbPointsSharedModule],
  declarations: [EventComponent, EventDetailComponent, EventUpdateComponent, EventDeleteDialogComponent, EventDeletePopupComponent],
  entryComponents: [EventDeleteDialogComponent]
})
export class PbPointsEventModule {}
