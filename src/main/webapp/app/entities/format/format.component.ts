import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IFormat } from 'app/shared/model/format.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FormatService } from './format.service';

@Component({
  selector: 'jhi-format',
  templateUrl: './format.component.html'
})
export class FormatComponent implements OnInit, OnDestroy {
  currentAccount: any;
  formats: IFormat[];
  eventSubscriber: Subscription;
  links: any;
  totalItems: number;
  itemsPerPage: number;
  page: any;
  predicate: any;
  reverse: any;
  tourId: number;
  private sub: any;

  constructor(
    protected formatService: FormatService,
    protected eventManager: JhiEventManager,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.formats = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
  if(this.tourId)
  {
    this.formatService
      .query({
       'tournamentId.equals': this.tourId,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFormat[]>) => this.paginateFormats(res.body, res.headers));
  }
  else
  {
  	this.formatService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFormat[]>) => this.paginateFormats(res.body, res.headers));
  }
  
  }

  reset() {
    this.page = 0;
    this.formats = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
  this.sub = this.activatedRoute
      .queryParams
      .subscribe(params => {
        this.tourId = +params['tourId'] || 0;
      });
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFormats();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFormat) {
    return item.id;
  }

  registerChangeInFormats() {
    this.eventSubscriber = this.eventManager.subscribe('formatListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFormats(data: IFormat[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.formats.push(data[i]);
    }
  }
  
  Cancel() {
    window.history.back();
  }
}
