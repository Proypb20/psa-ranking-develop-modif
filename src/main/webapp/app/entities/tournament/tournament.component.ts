import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';

import { ITournament } from 'app/shared/model/tournament.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TournamentService } from './tournament.service';

@Component({
  selector: 'jhi-tournament',
  templateUrl: './tournament.component.html'
})
export class TournamentComponent implements OnInit, OnDestroy {
  currentAccount: any;
  tournaments: ITournament[];
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

  constructor(
    protected tournamentService: TournamentService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
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
    if (this.currentAccount.authorities.includes('ROLE_ADMIN'))
    {
        this.tournamentService
          .query({
           page: this.page - 1,
           size: this.itemsPerPage,
           sort: this.sort()
           })
          .subscribe((res: HttpResponse<ITournament[]>) => this.paginateTournaments(res.body, res.headers));
    }
    else
    {
      if (this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT'))
      {
        this.tournamentService
            .query({
            "ownerId.equals": this.currentAccount.id,
             page: this.page - 1,
             size: this.itemsPerPage,
             sort: this.sort()
             })
            .subscribe((res: HttpResponse<ITournament[]>) => this.paginateTournaments(res.body, res.headers));
      }
      else
      {
        if (this.currentAccount.authorities.includes('ROLE_USER'))
        {
          this.tournamentService
              .query({
              'status.in': ['CREATED','IN_PROGRESS'],
               page: this.page - 1,
               size: this.itemsPerPage,
               sort: this.sort()
               })
              .subscribe((res: HttpResponse<ITournament[]>) => this.paginateTournaments(res.body, res.headers));
        }
        else
        {
          this.tournamentService
              .query({
               page: this.page - 1,
               size: this.itemsPerPage,
               sort: this.sort()
               })
             .subscribe((res: HttpResponse<ITournament[]>) => this.paginateTournaments(res.body, res.headers));
        }
      }
    }
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/tournament'], {
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
      '/tournament',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
      this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.loadAll();
    this.registerChangeInTournaments();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITournament) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInTournaments() {
    this.eventSubscriber = this.eventManager.subscribe('tournamentListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTournaments(data: ITournament[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.tournaments = data;
  }
}
