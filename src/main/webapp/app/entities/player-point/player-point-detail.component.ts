import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerPoint } from 'app/shared/model/player-point.model';

@Component({
  selector: 'jhi-player-point-detail',
  templateUrl: './player-point-detail.component.html'
})
export class PlayerPointDetailComponent implements OnInit {
  playerPoint: IPlayerPoint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerPoint }) => {
      this.playerPoint = playerPoint;
    });
  }

  previousState() {
    window.history.back();
  }
}
