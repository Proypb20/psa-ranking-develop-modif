import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TeamPoint } from 'app/shared/model/team-point.model';
import { TeamPointService } from './team-point.service';
import { TeamPointComponent } from './team-point.component';
import { TeamPointDetailComponent } from './team-point-detail.component';
import { TeamPointUpdateComponent } from './team-point-update.component';
import { TeamPointDeletePopupComponent } from './team-point-delete-dialog.component';
import { ITeamPoint } from 'app/shared/model/team-point.model';

@Injectable({ providedIn: 'root' })
export class TeamPointResolve implements Resolve<ITeamPoint> {
  constructor(private service: TeamPointService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITeamPoint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TeamPoint>) => response.ok),
        map((teamPoint: HttpResponse<TeamPoint>) => teamPoint.body)
      );
    }
    return of(new TeamPoint());
  }
}

export const teamPointRoute: Routes = [
  {
    path: '',
    component: TeamPointComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'psaRankingApp.teamPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TeamPointDetailComponent,
    resolve: {
      teamPoint: TeamPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TeamPointUpdateComponent,
    resolve: {
      teamPoint: TeamPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TeamPointUpdateComponent,
    resolve: {
      teamPoint: TeamPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const teamPointPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TeamPointDeletePopupComponent,
    resolve: {
      teamPoint: TeamPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamPoint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
