import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EventCategory } from 'app/shared/model/event-category.model';
import { EventCategoryService } from './event-category.service';
import { EventCategoryComponent } from './event-category.component';
import { EventCategoryDetailComponent } from './event-category-detail.component';
import { EventCategoryUpdateComponent } from './event-category-update.component';
import { EventCategoryDeletePopupComponent } from './event-category-delete-dialog.component';
import { IEventCategory } from 'app/shared/model/event-category.model';

@Injectable({ providedIn: 'root' })
export class EventCategoryResolve implements Resolve<IEventCategory> {
  constructor(private service: EventCategoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEventCategory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<EventCategory>) => response.ok),
        map((eventCategory: HttpResponse<EventCategory>) => eventCategory.body)
      );
    }
    return of(new EventCategory());
  }
}

export const eventCategoryRoute: Routes = [
  {
    path: '',
    component: EventCategoryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'psaRankingApp.eventCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EventCategoryDetailComponent,
    resolve: {
      eventCategory: EventCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.eventCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EventCategoryUpdateComponent,
    resolve: {
      eventCategory: EventCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.eventCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EventCategoryUpdateComponent,
    resolve: {
      eventCategory: EventCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.eventCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const eventCategoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EventCategoryDeletePopupComponent,
    resolve: {
      eventCategory: EventCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.eventCategory.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
