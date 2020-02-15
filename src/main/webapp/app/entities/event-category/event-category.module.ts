import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';


import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { EventCategoryComponent } from './event-category.component';
import { EventCategoryDetailComponent } from './event-category-detail.component';
import { EventCategoryUpdateComponent } from './event-category-update.component';
import { EventCategoryDeletePopupComponent, EventCategoryDeleteDialogComponent } from './event-category-delete-dialog.component';
import { eventCategoryRoute, eventCategoryPopupRoute } from './event-category.route';
import { TournamentFilterPipe } from 'app/core/custom/tournament-filter.pipe';

const ENTITY_STATES = [...eventCategoryRoute, ...eventCategoryPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EventCategoryComponent,
    EventCategoryDetailComponent,
    EventCategoryUpdateComponent,
    EventCategoryDeleteDialogComponent,
    EventCategoryDeletePopupComponent,
    TournamentFilterPipe 
  ],
  entryComponents: [EventCategoryDeleteDialogComponent],
  exports: [EventCategoryUpdateComponent]
})
export class PsaRankingEventCategoryModule {}
