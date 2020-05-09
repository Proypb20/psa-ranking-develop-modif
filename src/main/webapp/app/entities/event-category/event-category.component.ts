import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IEventCategory } from 'app/shared/model/event-category.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EventCategoryService } from './event-category.service';

@Component({
  selector: 'jhi-event-category',
  templateUrl: './event-category.component.html'
})
export class EventCategoryComponent implements OnInit, OnDestroy {
  currentAccount: any;
  eventCategories: IEventCategory[];
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
  evId: number;
  private sub: any;
  updAllowed: any;
  updateAllow: boolean;

  constructor(
    protected eventCategoryService: EventCategoryService,
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
    if (this.evId) {
      this.eventCategoryService
        .query({
          'eventId.equals': this.evId,
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IEventCategory[]>) => this.paginateEventCategories(res.body, res.headers));
    } else {
      this.eventCategoryService
        .query({
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IEventCategory[]>) => this.paginateEventCategories(res.body, res.headers));
    }
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/event-category'], {
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
      '/event-category',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.sub = this.activatedRoute.queryParams.subscribe(params => {
      // Defaults to 0 if no query param provided.
      this.evId = +params['evId'] || 0;
    });
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEventCategories();
    this.eventCategoryService.enableUpdate(this.evId).subscribe((res: HttpResponse<number>) => this.paginateUpdate(res.body, res.headers));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEventCategory) {
    return item.id;
  }

  registerChangeInEventCategories() {
    this.eventSubscriber = this.eventManager.subscribe('eventCategoryListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  paginateEventCategories(data: IEventCategory[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.eventCategories = data;
  }

  Cancel() {
    window.history.back();
  }

  enableUpdate() {
    return this.updateAllow;
  }

  protected paginateUpdate(data: number, headers: HttpHeaders) {
    this.updAllowed = data;
    if (this.updAllowed.toString() !== '0') this.updateAllow = true;
    else this.updateAllow = false;
  }
}
