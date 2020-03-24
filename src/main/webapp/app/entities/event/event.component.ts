import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IEvent } from 'app/shared/model/event.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EventService } from './event.service';

import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-event',
  templateUrl: './event.component.html'
})
export class EventComponent implements OnInit, OnDestroy {
  currentAccount: any;
  events: IEvent[];
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
  tourId: number;
  private sub: any;
  
  tournaments: ITournament[];
  cities: ICity[];

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tournamentService: TournamentService,
    protected cityService: CityService,
    protected eventService: EventService,
    protected parseLinks: JhiParseLinks,
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
  if(this.tourId)
  {
    this.eventService
      .query({
        'tournamentId.equals': this.tourId,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IEvent[]>) => this.paginateEvents(res.body, res.headers));
  }
  else
  { this.eventService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IEvent[]>) => this.paginateEvents(res.body, res.headers));
      }
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/event'], {
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
      '/event',
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
        this.tourId = +params['tourId'] || 0;
      });
    localStorage.setItem("TOURNAMENTID",this.tourId.toString());
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEvents();
    this.tournamentService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
	      map((response: HttpResponse<ITournament[]>) => response.body)
	    )
	    .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
	this.cityService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
	      map((response: HttpResponse<ICity[]>) => response.body)
	    )
	    .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEvent) {
    return item.id;
  }

  registerChangeInEvents() {
    this.eventSubscriber = this.eventManager.subscribe('eventListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEvents(data: IEvent[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.events = data;
  }
  
  trackTournamentById(index: number, item: ITournament) {
	    return item.name;
  }
  
  trackCityById(index: number, item: ICity) {
	    return item.name;
  }

  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
  
  generateXML(id: number)
  {
     this.subscribeToSaveResponse(this.eventService.generateXML(id));
  }
  
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }
  
  protected onSaveSuccess() {
    alert ("XML Generado con exito");
  }

  protected onSaveError() {
    alert("Error al generar XML");
  }
}
