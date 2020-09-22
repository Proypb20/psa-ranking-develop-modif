import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IFormula } from 'app/shared/model/formula.model';
import { AccountService } from 'app/core/auth/account.service';
import { FormulaService } from './formula.service';

@Component({
  selector: 'jhi-formula',
  templateUrl: './formula.component.html'
})
export class FormulaComponent implements OnInit, OnDestroy {
  formulas: IFormula[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected formulaService: FormulaService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.formulaService
      .query()
      .pipe(
        filter((res: HttpResponse<IFormula[]>) => res.ok),
        map((res: HttpResponse<IFormula[]>) => res.body)
      )
      .subscribe((res: IFormula[]) => {
        this.formulas = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFormulas();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFormula) {
    return item.id;
  }

  registerChangeInFormulas() {
    this.eventSubscriber = this.eventManager.subscribe('formulaListModification', response => this.loadAll());
  }
}
