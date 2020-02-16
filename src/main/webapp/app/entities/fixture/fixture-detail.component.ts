import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixture } from 'app/shared/model/fixture.model';

@Component({
  selector: 'jhi-fixture-detail',
  templateUrl: './fixture-detail.component.html'
})
export class FixtureDetailComponent implements OnInit {
  fixture: IFixture;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ fixture }) => {
      this.fixture = fixture;
    });
  }

  previousState() {
    window.history.back();
  }
}
