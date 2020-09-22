import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
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
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  reverse: any;
  currentImage : any;
  currentImageURL : any;

  constructor(
    protected playerPointService: PlayerPointService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected eventManager: JhiEventManager,
    private sanitizer: DomSanitizer
  ) {
    this.playerPoints = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
  if (this.currentAccount.authorities.includes('ROLE_ADMIN'))
  {
    this.playerPointService
      .query({
        page: this.page,
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

  reset() {
    this.page = 0;
    this.playerPoints = [];
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
    this.eventSubscriber = this.eventManager.subscribe('playerPointListModification', response => this.reset());
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
    for (let i = 0; i < data.length; i++) {
      this.playerPoints.push(data[i]);
    }
  }

  Cancel(){
      window.history.back();
  }
}
