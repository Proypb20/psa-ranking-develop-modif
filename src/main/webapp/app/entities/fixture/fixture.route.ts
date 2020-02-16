import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Fixture } from 'app/shared/model/fixture.model';
import { FixtureService } from './fixture.service';
import { FixtureComponent } from './fixture.component';
import { FixtureDetailComponent } from './fixture-detail.component';
import { FixtureUpdateComponent } from './fixture-update.component';
import { FixtureDeletePopupComponent } from './fixture-delete-dialog.component';
import { IFixture } from 'app/shared/model/fixture.model';

@Injectable({ providedIn: 'root' })
export class FixtureResolve implements Resolve<IFixture> {
  constructor(private service: FixtureService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFixture> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Fixture>) => response.ok),
        map((fixture: HttpResponse<Fixture>) => fixture.body)
      );
    }
    return of(new Fixture());
  }
}

export const fixtureRoute: Routes = [
  {
    path: '',
    component: FixtureComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'psaRankingApp.fixture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FixtureDetailComponent,
    resolve: {
      fixture: FixtureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.fixture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FixtureUpdateComponent,
    resolve: {
      fixture: FixtureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.fixture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FixtureUpdateComponent,
    resolve: {
      fixture: FixtureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.fixture.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const fixturePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FixtureDeletePopupComponent,
    resolve: {
      fixture: FixtureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.fixture.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
