import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Player } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';
import { PlayerComponent } from './player.component';
import { PlayerDetailComponent } from './player-detail.component';
import { PlayerUpdateComponent } from './player-update.component';
import { PlayerDeletePopupComponent } from './player-delete-dialog.component';
import { IPlayer } from 'app/shared/model/player.model';

@Injectable({ providedIn: 'root' })
export class PlayerResolve implements Resolve<IPlayer> {
  constructor(private service: PlayerService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlayer> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Player>) => response.ok),
        map((player: HttpResponse<Player>) => player.body)
      );
    }
    return of(new Player());
  }
}

export const playerRoute: Routes = [
  {
    path: '',
    component: PlayerComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PbPointsApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerDetailComponent,
    resolve: {
      player: PlayerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerUpdateComponent,
    resolve: {
      player: PlayerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerUpdateComponent,
    resolve: {
      player: PlayerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const playerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlayerDeletePopupComponent,
    resolve: {
      player: PlayerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.player.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
