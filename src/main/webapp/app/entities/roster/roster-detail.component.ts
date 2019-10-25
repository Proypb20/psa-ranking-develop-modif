import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoster } from 'app/shared/model/roster.model';

@Component({
  selector: 'jhi-roster-detail',
  templateUrl: './roster-detail.component.html'
})
export class RosterDetailComponent implements OnInit {
  roster: IRoster;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.roster = roster;
    });
  }

  previousState() {
    window.history.back();
  }
}
