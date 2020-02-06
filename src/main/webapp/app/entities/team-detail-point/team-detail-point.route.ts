import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TeamDetailPoint } from 'app/shared/model/team-detail-point.model';
import { TeamDetailPointService } from './team-detail-point.service';
import { TeamDetailPointComponent } from './team-detail-point.component';
import { TeamDetailPointDetailComponent } from './team-detail-point-detail.component';
import { TeamDetailPointUpdateComponent } from './team-detail-point-update.component';
import { TeamDetailPointDeletePopupComponent } from './team-detail-point-delete-dialog.component';
import { ITeamDetailPoint } from 'app/shared/model/team-detail-point.model';

@Injectable({ providedIn: 'root' })
export class TeamDetailPointResolve implements Resolve<ITeamDetailPoint> {
  constructor(private service: TeamDetailPointService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITeamDetailPoint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TeamDetailPoint>) => response.ok),
        map((teamDetailPoint: HttpResponse<TeamDetailPoint>) => teamDetailPoint.body)
      );
    }
    return of(new TeamDetailPoint());
  }
}

export const teamDetailPointRoute: Routes = [
  {
    path: '',
    component: TeamDetailPointComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'psaRankingApp.teamDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TeamDetailPointDetailComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TeamDetailPointUpdateComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TeamDetailPointUpdateComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const teamDetailPointPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TeamDetailPointDeletePopupComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.teamDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
