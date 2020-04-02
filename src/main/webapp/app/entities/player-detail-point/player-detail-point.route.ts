import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlayerDetailPoint } from 'app/shared/model/player-detail-point.model';
import { PlayerDetailPointService } from './player-detail-point.service';
import { PlayerDetailPointComponent } from './player-detail-point.component';
import { PlayerDetailPointDetailComponent } from './player-detail-point-detail.component';
import { PlayerDetailPointUpdateComponent } from './player-detail-point-update.component';
import { PlayerDetailPointDeletePopupComponent } from './player-detail-point-delete-dialog.component';
import { IPlayerDetailPoint } from 'app/shared/model/player-detail-point.model';

@Injectable({ providedIn: 'root' })
export class PlayerDetailPointResolve implements Resolve<IPlayerDetailPoint> {
  constructor(private service: PlayerDetailPointService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlayerDetailPoint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PlayerDetailPoint>) => response.ok),
        map((playerDetailPoint: HttpResponse<PlayerDetailPoint>) => playerDetailPoint.body)
      );
    }
    return of(new PlayerDetailPoint());
  }
}

export const playerDetailPointRoute: Routes = [
  {
    path: '',
    component: PlayerDetailPointComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PbPointsApp.playerDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerDetailPointDetailComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerDetailPointUpdateComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerDetailPointUpdateComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const playerDetailPointPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlayerDetailPointDeletePopupComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerDetailPoint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
