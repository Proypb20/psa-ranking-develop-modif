import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Bracket } from 'app/shared/model/bracket.model';
import { BracketService } from './bracket.service';
import { BracketComponent } from './bracket.component';
import { BracketDetailComponent } from './bracket-detail.component';
import { IBracket } from 'app/shared/model/bracket.model';

@Injectable({ providedIn: 'root' })
export class BracketResolve implements Resolve<IBracket> {
  constructor(private service: BracketService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBracket> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Bracket>) => response.ok),
        map((bracket: HttpResponse<Bracket>) => bracket.body)
      );
    }
    return of(new Bracket());
  }
}

export const bracketRoute: Routes = [
  {
    path: '',
    component: BracketComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.bracket.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BracketDetailComponent,
    resolve: {
      bracket: BracketResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'pbPointsApp.bracket.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const bracketPopupRoute: Routes = [];
