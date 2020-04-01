import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IPlayerPoint } from 'app/shared/model/player-point.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PlayerPointService } from './player-point.service';

import { DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'jhi-player-point',
  templateUrl: './player-point.component.html'
})
export class PlayerPointComponent implements OnInit, OnDestroy {
  currentAccount: any;
  playerPoints: IPlayerPoint[];
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
  currentImage : any;
  currentImageURL : any;

  constructor(
    protected playerPointService: PlayerPointService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private sanitizer: DomSanitizer
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
    this.playerPointService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPlayerPoint[]>) => this.paginatePlayerPoints(res.body, res.headers));
  }
  else
  {
  	 this.playerPointService
      .query({
       'userId.equals': this.currentAccount.id,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPlayerPoint[]>) => this.paginatePlayerPoints(res.body, res.headers));
  }
  
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/player-point'], {
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
      '/player-point',
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
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.currentImageURL = 'data:' + this.currentAccount.pictureContentType + ';base64,' + this.currentAccount.picture;
	this.currentImage = this.sanitizer.bypassSecurityTrustUrl(this.currentImageURL);
    this.registerChangeInPlayerPoints();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayerPoint) {
    return item.id;
  }

  registerChangeInPlayerPoints() {
    this.eventSubscriber = this.eventManager.subscribe('playerPointListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePlayerPoints(data: IPlayerPoint[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.playerPoints = data;
  }
  
  Cancel(){
      window.history.back();
  }
}
