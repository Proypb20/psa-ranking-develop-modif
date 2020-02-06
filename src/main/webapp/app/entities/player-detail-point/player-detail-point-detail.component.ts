import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerDetailPoint } from 'app/shared/model/player-detail-point.model';

@Component({
  selector: 'jhi-player-detail-point-detail',
  templateUrl: './player-detail-point-detail.component.html'
})
export class PlayerDetailPointDetailComponent implements OnInit {
  playerDetailPoint: IPlayerDetailPoint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerDetailPoint }) => {
      this.playerDetailPoint = playerDetailPoint;
    });
  }

  previousState() {
    window.history.back();
  }
}
