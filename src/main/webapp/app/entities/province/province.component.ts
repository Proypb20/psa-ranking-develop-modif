import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute} from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Location } from '@angular/common';
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
  links: any;
  totalItems: number;
  itemsPerPage: number;
  page: any;
  predicate: any;
  reverse: any;
  coId: number;
  private sub: any;

  countries: ICountry[];
  
  constructor(
    protected jhiAlertService: JhiAlertService,
    protected countryService: CountryService,
    protected provinceService: ProvinceService,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected location: Location,
    protected eventManager: JhiEventManager
  ) {
    this.provinces = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
  if(this.coId)
  {
    this.provinceService
      .query({
       'countryId.equals': this.coId,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IProvince[]>) => this.paginateProvinces(res.body, res.headers));
  }
  else
  {
  	this.provinceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IProvince[]>) => this.paginateProvinces(res.body, res.headers));
  }
  }

  reset() {
    this.page = 0;
    this.provinces = [];
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
        this.coId = +params['coId'] || 0;
      });
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
    this.eventSubscriber = this.eventManager.subscribe('provinceListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProvinces(data: IProvince[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.provinces.push(data[i]);
    }
  }
 
  trackCountryById(index: number, item: ICountry) {
	    return item.name;
  }

  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
  
  protected Cancel(){
      this.location.back();
  }
}
