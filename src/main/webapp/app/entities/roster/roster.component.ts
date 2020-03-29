import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { IEventCategory } from 'app/shared/model/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/event-category.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IRoster } from 'app/shared/model/roster.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RosterService } from './roster.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-roster',
  templateUrl: './roster.component.html'
})
export class RosterComponent implements OnInit, OnDestroy {
  currentAccount: any;
  rosters: IRoster[];
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
  teId: number;
  evCatId: number;
  private sub: any;
  eventCategories: IEventCategory[];
  events: IEvent[];
  categories: ICategory[];

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected eventService: EventService,
    protected eventCategoryService: EventCategoryService,
    protected categoryService: CategoryService,
    protected rosterService: RosterService,
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
    if (this.evCatId)
    {
        this.rosterService
            .query({
                  'eventCategoryId.equals': this.evCatId,
                   page: this.page - 1,
                   size: this.itemsPerPage,
                   sort: this.sort()})
            .subscribe((res: HttpResponse<IRoster[]>) => this.paginateRosters(res.body, res.headers));
    }
    else
    {
       if (this.teId)
       {
        this.rosterService
            .query({
             'teamId.equals': this.teId,
              page: this.page - 1,
              size: this.itemsPerPage,
              sort: this.sort()})
            .subscribe((res: HttpResponse<IRoster[]>) => this.paginateRosters(res.body, res.headers));
        }
        else
        {
         this.rosterService
             .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()})
             .subscribe((res: HttpResponse<IRoster[]>) => this.paginateRosters(res.body, res.headers));
        }
    }
    this.eventService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
	      map((response: HttpResponse<IEvent[]>) => response.body)
	    )
	    .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
	this.eventCategoryService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<IEventCategory[]>) => mayBeOk.ok),
	      map((response: HttpResponse<IEventCategory[]>) => response.body)
	    )
	    .subscribe((res: IEventCategory[]) => (this.eventCategories = res), (res: HttpErrorResponse) => this.onError(res.message));
	this.categoryService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
	      map((response: HttpResponse<ICategory[]>) => response.body)
	    )
	    .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/roster'], {
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
      '/roster',
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
    this.sub = this.activatedRoute
      .queryParams
      .subscribe(params => {
        this.teId = +params['teamId'] || 0;
        this.evCatId = +params['evCatId'] || 0;
      });
    this.loadAll();
    this.registerChangeInRosters();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRoster) {
    return item.id;
  }

  registerChangeInRosters() {
    this.eventSubscriber = this.eventManager.subscribe('rosterListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRosters(data: IRoster[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.rosters = data;
  }
  
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
