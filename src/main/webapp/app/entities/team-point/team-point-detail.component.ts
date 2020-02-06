import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamPoint } from 'app/shared/model/team-point.model';

@Component({
  selector: 'jhi-team-point-detail',
  templateUrl: './team-point-detail.component.html'
})
export class TeamPointDetailComponent implements OnInit {
  teamPoint: ITeamPoint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ teamPoint }) => {
      this.teamPoint = teamPoint;
    });
  }

  previousState() {
    window.history.back();
  }
}
