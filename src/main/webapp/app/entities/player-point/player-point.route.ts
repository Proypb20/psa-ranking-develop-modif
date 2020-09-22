import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlayerPoint } from 'app/shared/model/player-point.model';
import { PlayerPointService } from './player-point.service';
import { PlayerPointComponent } from './player-point.component';
import { PlayerPointDetailComponent } from './player-point-detail.component';
import { PlayerPointUpdateComponent } from './player-point-update.component';
import { PlayerPointDeletePopupComponent } from './player-point-delete-dialog.component';
import { IPlayerPoint } from 'app/shared/model/player-point.model';

@Injectable({ providedIn: 'root' })
export class PlayerPointResolve implements Resolve<IPlayerPoint> {
  constructor(private service: PlayerPointService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlayerPoint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PlayerPoint>) => response.ok),
        map((playerPoint: HttpResponse<PlayerPoint>) => playerPoint.body)
      );
    }
    return of(new PlayerPoint());
  }
}

export const playerPointRoute: Routes = [
  {
    path: '',
    component: PlayerPointComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerPointDetailComponent,
    resolve: {
      playerPoint: PlayerPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerPointUpdateComponent,
    resolve: {
      playerPoint: PlayerPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerPointUpdateComponent,
    resolve: {
      playerPoint: PlayerPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerPoint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const playerPointPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlayerPointDeletePopupComponent,
    resolve: {
      playerPoint: PlayerPointResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.playerPoint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
