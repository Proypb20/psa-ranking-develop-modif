import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ActivatedRoute } from '@angular/router';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account;
  authSubscription: Subscription;
  modalRef: NgbModalRef;
  events: IEvent[];
  tournaments: ITournament[];
  cities: ICity[];
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    private accountService: AccountService,
    private jhiAlertService: JhiAlertService,
    private loginModalService: LoginModalService,
    private parseLinks: JhiParseLinks,
    private tournamentService: TournamentService,
    private cityService: CityService,
    private eventService: EventService,
    private activatedRoute: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {
	  this.itemsPerPage = ITEMS_PER_PAGE;
	  this.routeData = this.activatedRoute.data.subscribe(data => {
	      this.page = data.pagingParams.page;
	      this.previousPage = data.pagingParams.page;
	      this.reverse = data.pagingParams.ascending;
	      this.predicate = data.pagingParams.predicate;
	    });
  }

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.eventService.query({
              'status.in': ['CREATED','IN_PROGRESS'],
					 page:  1 - 1,
					 size:  200
					})
		      .subscribe((res: HttpResponse<IEvent[]>) => this.paginateEvents(res.body, res.headers));
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
    this.registerAuthenticationSuccess();
  }
  
    trackId(index: number, item: IEvent) {
    return item.id;
  }
  
  
  protected paginateEvents(data: IEvent[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.events = data;
  }
  
  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }
  
  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
  
}
