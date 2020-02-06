import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerDetailPoint, PlayerDetailPoint } from 'app/shared/model/player-detail-point.model';
import { PlayerDetailPointService } from './player-detail-point.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { IPlayerPoint } from 'app/shared/model/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/player-point.service';

@Component({
  selector: 'jhi-player-detail-point-update',
  templateUrl: './player-detail-point-update.component.html'
})
export class PlayerDetailPointUpdateComponent implements OnInit {
  isSaving: boolean;

  events: IEvent[];

  playerpoints: IPlayerPoint[];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    eventId: [null, Validators.required],
    playerPointId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerDetailPointService: PlayerDetailPointService,
    protected eventService: EventService,
    protected playerPointService: PlayerPointService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ playerDetailPoint }) => {
      this.updateForm(playerDetailPoint);
    });
    this.eventService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerPointService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayerPoint[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayerPoint[]>) => response.body)
      )
      .subscribe((res: IPlayerPoint[]) => (this.playerpoints = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playerDetailPoint: IPlayerDetailPoint) {
    this.editForm.patchValue({
      id: playerDetailPoint.id,
      points: playerDetailPoint.points,
      eventId: playerDetailPoint.eventId,
      playerPointId: playerDetailPoint.playerPointId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const playerDetailPoint = this.createFromForm();
    if (playerDetailPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.playerDetailPointService.update(playerDetailPoint));
    } else {
      this.subscribeToSaveResponse(this.playerDetailPointService.create(playerDetailPoint));
    }
  }

  private createFromForm(): IPlayerDetailPoint {
    return {
      ...new PlayerDetailPoint(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      eventId: this.editForm.get(['eventId']).value,
      playerPointId: this.editForm.get(['playerPointId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerDetailPoint>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackEventById(index: number, item: IEvent) {
    return item.id;
  }

  trackPlayerPointById(index: number, item: IPlayerPoint) {
    return item.id;
  }
}
