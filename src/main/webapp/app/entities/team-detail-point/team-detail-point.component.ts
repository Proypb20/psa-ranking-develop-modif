import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Location } from '@angular/common';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { ITeamDetailPoint } from 'app/shared/model/team-detail-point.model';
import { AccountService } from 'app/core/auth/account.service';
import { JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TeamDetailPointService } from './team-detail-point.service';

@Component({
  selector: 'jhi-team-detail-point',
  templateUrl: './team-detail-point.component.html'
})
export class TeamDetailPointComponent implements OnInit, OnDestroy {
  currentAccount: any;
  teamDetailPoints: ITeamDetailPoint[];
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
  tpId: number;
  private sub: any;
  tournaments: ITournament[];
  events: IEvent[];

  constructor(
    protected teamDetailPointService: TeamDetailPointService,
    protected tournamentService: TournamentService,
    protected eventService: EventService,
    protected jhiAlertService: JhiAlertService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected location: Location,
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
    this.teamDetailPointService
      .query({
       'teamPointId.equals': this.tpId,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITeamDetailPoint[]>) => this.paginateTeamDetailPoints(res.body, res.headers));
    this.tournamentService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
	      map((response: HttpResponse<ITournament[]>) => response.body)
	    )
	    .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
	this.eventService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
	      map((response: HttpResponse<IEvent[]>) => response.body)
	    )
	    .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));  
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/team-detail-point'], {
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
      '/team-detail-point',
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
        this.tpId = +params['tpId'] || 0;
      });
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTeamDetailPoints();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITeamDetailPoint) {
    return item.id;
  }

  registerChangeInTeamDetailPoints() {
    this.eventSubscriber = this.eventManager.subscribe('teamDetailPointListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTeamDetailPoints(data: ITeamDetailPoint[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.teamDetailPoints = data;
  }
  
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
  
  Cancel(){
      this.location.back();
  }
}
