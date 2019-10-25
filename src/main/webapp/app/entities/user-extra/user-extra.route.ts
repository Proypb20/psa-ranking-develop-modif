import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from './user-extra.service';
import { UserExtraComponent } from './user-extra.component';
import { UserExtraDetailComponent } from './user-extra-detail.component';
import { UserExtraUpdateComponent } from './user-extra-update.component';
import { UserExtraDeletePopupComponent } from './user-extra-delete-dialog.component';
import { IUserExtra } from 'app/shared/model/user-extra.model';

@Injectable({ providedIn: 'root' })
export class UserExtraResolve implements Resolve<IUserExtra> {
  constructor(private service: UserExtraService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserExtra> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserExtra>) => response.ok),
        map((userExtra: HttpResponse<UserExtra>) => userExtra.body)
      );
    }
    return of(new UserExtra());
  }
}

export const userExtraRoute: Routes = [
  {
    path: '',
    component: UserExtraComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'psaRankingApp.userExtra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserExtraDetailComponent,
    resolve: {
      userExtra: UserExtraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.userExtra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserExtraUpdateComponent,
    resolve: {
      userExtra: UserExtraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.userExtra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserExtraUpdateComponent,
    resolve: {
      userExtra: UserExtraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.userExtra.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userExtraPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserExtraDeletePopupComponent,
    resolve: {
      userExtra: UserExtraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.userExtra.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
