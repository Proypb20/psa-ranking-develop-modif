import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormula } from 'app/shared/model/formula.model';

@Component({
  selector: 'jhi-formula-detail',
  templateUrl: './formula-detail.component.html'
})
export class FormulaDetailComponent implements OnInit {
  formula: IFormula;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ formula }) => {
      this.formula = formula;
    });
  }

  previousState() {
    window.history.back();
  }
}
