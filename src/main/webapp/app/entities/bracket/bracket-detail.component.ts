import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBracket } from 'app/shared/model/bracket.model';

@Component({
  selector: 'jhi-bracket-detail',
  templateUrl: './bracket-detail.component.html'
})
export class BracketDetailComponent implements OnInit {
  bracket: IBracket;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ bracket }) => {
      this.bracket = bracket;
    });
  }

  previousState() {
    window.history.back();
  }
}
