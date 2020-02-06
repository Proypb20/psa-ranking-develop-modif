import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamDetailPoint } from 'app/shared/model/team-detail-point.model';

@Component({
  selector: 'jhi-team-detail-point-detail',
  templateUrl: './team-detail-point-detail.component.html'
})
export class TeamDetailPointDetailComponent implements OnInit {
  teamDetailPoint: ITeamDetailPoint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ teamDetailPoint }) => {
      this.teamDetailPoint = teamDetailPoint;
    });
  }

  previousState() {
    window.history.back();
  }
}
