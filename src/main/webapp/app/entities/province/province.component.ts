import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IProvince } from 'app/shared/model/province.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProvinceService } from './province.service';

import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country/country.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-province',
  templateUrl: './province.component.html'
})
export class ProvinceComponent implements OnInit, OnDestroy {

  currentAccount: any;
  provinces: IProvince[];
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

  countries: ICountry[];

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected countryService: CountryService,
    protected provinceService: ProvinceService,
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
    this.provinceService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IProvince[]>) => this.paginateProvinces(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/province'], {
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
      '/province',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInProvinces();
    this.countryService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<ICountry[]>) => mayBeOk.ok),
	      map((response: HttpResponse<ICountry[]>) => response.body)
	    )
	    .subscribe((res: ICountry[]) => (this.countries = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProvince) {
    return item.id;
  }

  registerChangeInProvinces() {
    this.eventSubscriber = this.eventManager.subscribe('provinceListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProvinces(data: IProvince[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.provinces = data;
  }
  
  trackCountryById(index: number, item: ICountry) {
	    return item.name;
  }

  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
}
