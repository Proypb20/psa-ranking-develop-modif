import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from './doc-type.service';
import { DocTypeComponent } from './doc-type.component';
import { DocTypeDetailComponent } from './doc-type-detail.component';
import { DocTypeUpdateComponent } from './doc-type-update.component';
import { DocTypeDeletePopupComponent } from './doc-type-delete-dialog.component';
import { IDocType } from 'app/shared/model/doc-type.model';

@Injectable({ providedIn: 'root' })
export class DocTypeResolve implements Resolve<IDocType> {
  constructor(private service: DocTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDocType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DocType>) => response.ok),
        map((docType: HttpResponse<DocType>) => docType.body)
      );
    }
    return of(new DocType());
  }
}

export const docTypeRoute: Routes = [
  {
    path: '',
    component: DocTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PbPointsApp.docType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocTypeDetailComponent,
    resolve: {
      docType: DocTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.docType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocTypeUpdateComponent,
    resolve: {
      docType: DocTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.docType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocTypeUpdateComponent,
    resolve: {
      docType: DocTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.docType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const docTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DocTypeDeletePopupComponent,
    resolve: {
      docType: DocTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PbPointsApp.docType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
