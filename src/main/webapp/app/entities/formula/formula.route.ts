import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Formula } from 'app/shared/model/formula.model';
import { FormulaService } from './formula.service';
import { FormulaComponent } from './formula.component';
import { FormulaDetailComponent } from './formula-detail.component';
import { FormulaUpdateComponent } from './formula-update.component';
import { FormulaDeletePopupComponent } from './formula-delete-dialog.component';
import { IFormula } from 'app/shared/model/formula.model';

@Injectable({ providedIn: 'root' })
export class FormulaResolve implements Resolve<IFormula> {
  constructor(private service: FormulaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFormula> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Formula>) => response.ok),
        map((formula: HttpResponse<Formula>) => formula.body)
      );
    }
    return of(new Formula());
  }
}

export const formulaRoute: Routes = [
  {
    path: '',
    component: FormulaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.formula.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FormulaDetailComponent,
    resolve: {
      formula: FormulaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.formula.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.formula.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.formula.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const formulaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FormulaDeletePopupComponent,
    resolve: {
      formula: FormulaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.formula.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
