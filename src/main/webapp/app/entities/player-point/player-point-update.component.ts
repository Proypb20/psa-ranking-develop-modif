import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerPoint, PlayerPoint } from 'app/shared/model/player-point.model';
import { PlayerPointService } from './player-point.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-player-point-update',
  templateUrl: './player-point-update.component.html'
})
export class PlayerPointUpdateComponent implements OnInit {
  isSaving: boolean;

  tournaments: ITournament[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    tournamentId: [null, Validators.required],
    userId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerPointService: PlayerPointService,
    protected tournamentService: TournamentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ playerPoint }) => {
      this.updateForm(playerPoint);
    });
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playerPoint: IPlayerPoint) {
    this.editForm.patchValue({
      id: playerPoint.id,
      points: playerPoint.points,
      tournamentId: playerPoint.tournamentId,
      userId: playerPoint.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const playerPoint = this.createFromForm();
    if (playerPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.playerPointService.update(playerPoint));
    } else {
      this.subscribeToSaveResponse(this.playerPointService.create(playerPoint));
    }
  }

  private createFromForm(): IPlayerPoint {
    return {
      ...new PlayerPoint(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      tournamentId: this.editForm.get(['tournamentId']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerPoint>>) {
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

  trackTournamentById(index: number, item: ITournament) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
