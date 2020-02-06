import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITeamDetailPoint, TeamDetailPoint } from 'app/shared/model/team-detail-point.model';
import { TeamDetailPointService } from './team-detail-point.service';
import { ITeamPoint } from 'app/shared/model/team-point.model';
import { TeamPointService } from 'app/entities/team-point/team-point.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';

@Component({
  selector: 'jhi-team-detail-point-update',
  templateUrl: './team-detail-point-update.component.html'
})
export class TeamDetailPointUpdateComponent implements OnInit {
  isSaving: boolean;

  teampoints: ITeamPoint[];

  events: IEvent[];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    teamPointId: [null, Validators.required],
    eventId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected teamDetailPointService: TeamDetailPointService,
    protected teamPointService: TeamPointService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ teamDetailPoint }) => {
      this.updateForm(teamDetailPoint);
    });
    this.teamPointService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeamPoint[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeamPoint[]>) => response.body)
      )
      .subscribe((res: ITeamPoint[]) => (this.teampoints = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.eventService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(teamDetailPoint: ITeamDetailPoint) {
    this.editForm.patchValue({
      id: teamDetailPoint.id,
      points: teamDetailPoint.points,
      teamPointId: teamDetailPoint.teamPointId,
      eventId: teamDetailPoint.eventId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const teamDetailPoint = this.createFromForm();
    if (teamDetailPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.teamDetailPointService.update(teamDetailPoint));
    } else {
      this.subscribeToSaveResponse(this.teamDetailPointService.create(teamDetailPoint));
    }
  }

  private createFromForm(): ITeamDetailPoint {
    return {
      ...new TeamDetailPoint(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      teamPointId: this.editForm.get(['teamPointId']).value,
      eventId: this.editForm.get(['eventId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamDetailPoint>>) {
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

  trackTeamPointById(index: number, item: ITeamPoint) {
    return item.id;
  }

  trackEventById(index: number, item: IEvent) {
    return item.id;
  }
}
