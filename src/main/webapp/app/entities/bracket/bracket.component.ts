import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IBracket } from 'app/shared/model/bracket.model';
import { AccountService } from 'app/core/auth/account.service';
import { BracketService } from './bracket.service';

@Component({
  selector: 'jhi-bracket',
  templateUrl: './bracket.component.html'
})
export class BracketComponent implements OnInit, OnDestroy {
  brackets: IBracket[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected bracketService: BracketService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.bracketService
      .query()
      .pipe(
        filter((res: HttpResponse<IBracket[]>) => res.ok),
        map((res: HttpResponse<IBracket[]>) => res.body)
      )
      .subscribe((res: IBracket[]) => {
        this.brackets = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInBrackets();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBracket) {
    return item.id;
  }

  registerChangeInBrackets() {
    this.eventSubscriber = this.eventManager.subscribe('bracketListModification', response => this.loadAll());
  }
}
