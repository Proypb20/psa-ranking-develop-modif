import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Roster } from 'app/shared/model/roster.model';
import { RosterService } from './roster.service';
import { RosterComponent } from './roster.component';
import { RosterDetailComponent } from './roster-detail.component';
import { RosterUpdateComponent } from './roster-update.component';
import { RosterDeletePopupComponent } from './roster-delete-dialog.component';
import { IRoster } from 'app/shared/model/roster.model';

@Injectable({ providedIn: 'root' })
export class RosterResolve implements Resolve<IRoster> {
  constructor(private service: RosterService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoster> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Roster>) => response.ok),
        map((roster: HttpResponse<Roster>) => roster.body)
      );
    }
    return of(new Roster());
  }
}

export const rosterRoute: Routes = [
  {
    path: '',
    component: RosterComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PbPointsApp.roster.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RosterDetailComponent,
    resolve: {
      roster: RosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.roster.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RosterUpdateComponent,
    resolve: {
      roster: RosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.roster.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RosterUpdateComponent,
    resolve: {
      roster: RosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.roster.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const rosterPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RosterDeletePopupComponent,
    resolve: {
      roster: RosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.roster.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
