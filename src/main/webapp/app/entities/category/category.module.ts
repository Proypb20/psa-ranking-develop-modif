import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { CategoryComponent } from './category.component';
import { CategoryDetailComponent } from './category-detail.component';
import { CategoryUpdateComponent } from './category-update.component';
import { CategoryDeletePopupComponent, CategoryDeleteDialogComponent } from './category-delete-dialog.component';
import { categoryRoute, categoryPopupRoute } from './category.route';

const ENTITY_STATES = [...categoryRoute, ...categoryPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CategoryComponent,
    CategoryDetailComponent,
    CategoryUpdateComponent,
    CategoryDeleteDialogComponent,
    CategoryDeletePopupComponent
  ],
  entryComponents: [CategoryDeleteDialogComponent]
})
export class PsaRankingCategoryModule {}
