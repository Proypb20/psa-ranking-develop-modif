import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPlayer } from 'app/shared/model/player.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PlayerService } from './player.service';

import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-player',
  templateUrl: './player.component.html'
})
export class PlayerComponent implements OnInit, OnDestroy {
  currentAccount: any;
  players: IPlayer[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  rId: number;
  private sub: any;
	
  users: IUser[];
  private completeName : any;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerService,
    protected parseLinks: JhiParseLinks,
    protected userService: UserService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
  if (this.rId)
  {
    this.playerService
      .query({
       'rosterId.equals' : this.rId,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPlayer[]>) => this.paginatePlayers(res.body, res.headers));
  }
  else
  {
    this.playerService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPlayer[]>) => this.paginatePlayers(res.body, res.headers));
   }
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/player'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/player',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
  this.sub = this.activatedRoute
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.rId = +params['rId'] || 0;
      });
    this.loadAll();
    this.userService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
	      map((response: HttpResponse<IUser[]>) => response.body)
	    )
	    .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPlayers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayer) {
    return item.id;
  }

  registerChangeInPlayers() {
    this.eventSubscriber = this.eventManager.subscribe('playerListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePlayers(data: IPlayer[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.players = data;
  }
  
  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
}
