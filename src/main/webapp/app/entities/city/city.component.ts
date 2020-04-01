import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute} from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ICity } from 'app/shared/model/city.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CityService } from './city.service';

import { IProvince } from 'app/shared/model/province.model';
import { ProvinceService } from 'app/entities/province/province.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-city',
  templateUrl: './city.component.html'
})
export class CityComponent implements OnInit, OnDestroy {

  currentAccount: any;
  cities: ICity[];
  eventSubscriber: Subscription;
  links: any;
  totalItems: number;
  itemsPerPage: number;
  page: any;
  predicate: any;
  reverse: any;
  pId: number;
  private sub: any;

  provinces: IProvince[];

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected provinceService: ProvinceService,
    protected cityService: CityService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected eventManager: JhiEventManager
  ) {
    this.cities = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
   if (this.pId)
   {
    this.cityService
      .query({
       'provinceId.equals': this.pId,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICity[]>) => this.paginateCities(res.body, res.headers));
    }
    else
    {
      this.cityService
          .query({
		        page: this.page,
		        size: this.itemsPerPage,
		        sort: this.sort()
      			})
           .subscribe((res: HttpResponse<ICity[]>) => this.paginateCities(res.body, res.headers));
    }
    
  }

  reset() {
    this.page = 0;
    this.cities = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
	  this.accountService.identity().subscribe(account => {
	      this.currentAccount = account;
	    });
  this.sub = this.activatedRoute
      .queryParams
      .subscribe(params => {
        this.pId = +params['pId'] || 0;
      });
    this.loadAll();
    this.registerChangeInCities();
    
    this.provinceService
	    .query({
	    	size: 2000
	    })
	    .pipe(
	      filter((mayBeOk: HttpResponse<IProvince[]>) => mayBeOk.ok),
	      map((response: HttpResponse<IProvince[]>) => response.body)
	    )
	    .subscribe((res: IProvince[]) => (this.provinces = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICity) {
    return item.id;
  }

  registerChangeInCities() {
    this.eventSubscriber = this.eventManager.subscribe('cityListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCities(data: ICity[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.cities.push(data[i]);
    }
  }
  
  trackProvinceById(index: number, item: IProvince) {
	    return item.name;
  }

  protected onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }
  
  Cancel() {
    window.history.back();
  }
}
