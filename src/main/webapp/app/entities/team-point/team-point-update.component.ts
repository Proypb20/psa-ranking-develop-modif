import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITeamPoint, TeamPoint } from 'app/shared/model/team-point.model';
import { TeamPointService } from './team-point.service';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';

@Component({
  selector: 'jhi-team-point-update',
  templateUrl: './team-point-update.component.html'
})
export class TeamPointUpdateComponent implements OnInit {
  isSaving: boolean;

  teams: ITeam[];

  tournaments: ITournament[];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    teamId: [null, Validators.required],
    tournamentId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected teamPointService: TeamPointService,
    protected teamService: TeamService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ teamPoint }) => {
      this.updateForm(teamPoint);
    });
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(teamPoint: ITeamPoint) {
    this.editForm.patchValue({
      id: teamPoint.id,
      points: teamPoint.points,
      teamId: teamPoint.teamId,
      tournamentId: teamPoint.tournamentId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const teamPoint = this.createFromForm();
    if (teamPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.teamPointService.update(teamPoint));
    } else {
      this.subscribeToSaveResponse(this.teamPointService.create(teamPoint));
    }
  }

  private createFromForm(): ITeamPoint {
    return {
      ...new TeamPoint(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      teamId: this.editForm.get(['teamId']).value,
      tournamentId: this.editForm.get(['tournamentId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamPoint>>) {
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

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }

  trackTournamentById(index: number, item: ITournament) {
    return item.id;
  }
}
