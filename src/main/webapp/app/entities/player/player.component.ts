import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IPlayer, Player } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';
import { IPlayerPoint } from 'app/shared/model/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/player-point.service';

import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-player',
  templateUrl: './player.component.html'
})
export class PlayerComponent implements OnInit, OnDestroy {
  currentAccount: any;
  isSaving: boolean;
  players: IPlayer[];
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
  rId: number;
  private sub: any;
  tourId: any;
  users: IUser[];
  users2: IUser[];
  playerpoints: IPlayerPoint[];
  ownerId: any;
  isOwner: boolean;
  updAllowed: any;
  tourSet: boolean;

  private completeName: any;

  addForm = this.fb.group({
    id: [],
    profile: [],
    userId: [],
    rosterId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerService,
    protected playerPointService: PlayerPointService,
    protected parseLinks: JhiParseLinks,
    protected userService: UserService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder
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
    if (this.rId) {
      this.playerService
        .query({
          'rosterId.equals': this.rId,
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IPlayer[]>) => this.paginatePlayers(res.body, res.headers));
    } else {
      this.playerService
        .query({
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IPlayer[]>) => this.paginatePlayers(res.body, res.headers));
    }
  }

  private createFromForm(): IPlayer {
    return {
      ...new Player(),
      id: this.addForm.get(['id']).value,
      profile: this.addForm.get(['profile']).value,
      userId: this.addForm.get(['userId']).value,
      rosterId: this.rId || this.addForm.get(['rosterId']).value
    };
  }

  save() {
    this.isSaving = true;
    const player = this.createFromForm();
    this.subscribeToSaveResponse(this.playerService.create(player));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/player'], {
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
      '/player',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.sub = this.activatedRoute.queryParams.subscribe(params => {
      this.rId = +params['rId'] || 0;
    });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.userService
      .query({
        size: 2000
      })
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));

    this.tourId = localStorage.getItem('TOURNAMENTID');
    if (this.tourId) {
      this.playerPointService
        .query({
          size: 2000,
          'tournamentId.equals': +this.tourId || 0
        })
        .pipe(
          filter((mayBeOk: HttpResponse<IPlayerPoint[]>) => mayBeOk.ok),
          map((response: HttpResponse<IPlayerPoint[]>) => response.body)
        )
        .subscribe((res: IPlayerPoint[]) => (this.playerpoints = res), (res: HttpErrorResponse) => this.onError(res.message));

      if (this.tourId === 0) this.tourSet = false;
      else this.tourSet = true;
    }

    this.loadAll();
    this.registerChangeInPlayers();
    this.playerService.findOwner(this.rId).subscribe((res: HttpResponse<number>) => this.paginateOwner(res.body, res.headers));
    this.playerService.enableUpdate(this.rId).subscribe((res: HttpResponse<number>) => this.paginateUpdate(res.body, res.headers));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayer) {
    return item.id;
  }

  registerChangeInPlayers() {
    this.eventSubscriber = this.eventManager.subscribe('playerListModification', response => this.loadAll());
  }

  getPlayerName() {
    this.userService
      .query({
        size: 2000,
        'id.equals': +this.addForm.get(['userId']).value
      })
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users2 = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePlayers(data: IPlayer[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.players = data;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    window.location.reload();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  Cancel() {
    window.history.back();
  }

  isTheOwner() {
    return this.isOwner;
  }

  protected paginateOwner(data: number, headers: HttpHeaders) {
    this.ownerId = data;
    if (this.ownerId.toString() === this.currentAccount.id.toString()) this.isOwner = true;
    else this.isOwner = false;
  }

  protected paginateUpdate(data: number, headers: HttpHeaders) {
    this.updAllowed = data;
    if (this.updAllowed.toString() !== '0')
      if (this.isOwner) this.isOwner = true;
      else this.isOwner = false;
    else this.isOwner = false;
  }

  IsTourSet() {
    return this.tourSet;
  }
}
