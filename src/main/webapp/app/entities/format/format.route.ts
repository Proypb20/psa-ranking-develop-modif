import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Format } from 'app/shared/model/format.model';
import { FormatService } from './format.service';
import { FormatComponent } from './format.component';
import { FormatDetailComponent } from './format-detail.component';
import { FormatUpdateComponent } from './format-update.component';
import { FormatDeletePopupComponent } from './format-delete-dialog.component';
import { IFormat } from 'app/shared/model/format.model';

@Injectable({ providedIn: 'root' })
export class FormatResolve implements Resolve<IFormat> {
  constructor(private service: FormatService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFormat> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Format>) => response.ok),
        map((format: HttpResponse<Format>) => format.body)
      );
    }
    return of(new Format());
  }
}

export const formatRoute: Routes = [
  {
    path: '',
    component: FormatComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.format.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FormatDetailComponent,
    resolve: {
      format: FormatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.format.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FormatUpdateComponent,
    resolve: {
      format: FormatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.format.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FormatUpdateComponent,
    resolve: {
      format: FormatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.format.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const formatPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FormatDeletePopupComponent,
    resolve: {
      format: FormatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'psaRankingApp.format.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
