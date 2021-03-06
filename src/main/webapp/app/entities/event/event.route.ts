import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Event } from 'app/shared/model/event.model';
import { EventService } from './event.service';
import { EventComponent } from './event.component';
import { EventDetailComponent } from './event-detail.component';
import { EventUpdateComponent } from './event-update.component';
import { EventDeletePopupComponent } from './event-delete-dialog.component';
import { IEvent } from 'app/shared/model/event.model';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class EventResolve implements Resolve<IEvent> {
  constructor(private service: EventService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEvent> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Event>) => response.ok),
        map((event: HttpResponse<Event>) => event.body)
      );
    }
    return of(new Event());
  }
}

export const eventRoute: Routes = [
  {
    path: '',
    component: EventComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PbPointsApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EventDetailComponent,
    resolve: {
      event: EventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EventUpdateComponent,
    resolve: {
      event: EventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EventUpdateComponent,
    resolve: {
      event: EventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const eventPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EventDeletePopupComponent,
    resolve: {
      event: EventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.event.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

const ROUTE = [...eventRoute, ...eventPopupRoute];
@NgModule({
    imports: [RouterModule.forChild(ROUTE)],
    exports: [RouterModule]
})
export class EvRoute { }