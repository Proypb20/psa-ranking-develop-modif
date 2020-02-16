import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IGame, Game } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { IFixture } from 'app/shared/model/fixture.model';
import { FixtureService } from 'app/entities/fixture/fixture.service';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html'
})
export class GameUpdateComponent implements OnInit {
  isSaving: boolean;

  fixtures: IFixture[];

  teams: ITeam[];

  editForm = this.fb.group({
    id: [],
    pointsA: [],
    pointsB: [],
    splitDeckNum: [],
    timeLeft: [],
    status: [null, [Validators.required]],
    fixtureId: [null, Validators.required],
    teamAId: [null, Validators.required],
    teamBId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected gameService: GameService,
    protected fixtureService: FixtureService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ game }) => {
      this.updateForm(game);
    });
    this.fixtureService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IFixture[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFixture[]>) => response.body)
      )
      .subscribe((res: IFixture[]) => (this.fixtures = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(game: IGame) {
    this.editForm.patchValue({
      id: game.id,
      pointsA: game.pointsA,
      pointsB: game.pointsB,
      splitDeckNum: game.splitDeckNum,
      timeLeft: game.timeLeft,
      status: game.status,
      fixtureId: game.fixtureId,
      teamAId: game.teamAId,
      teamBId: game.teamBId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const game = this.createFromForm();
    if (game.id !== undefined) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  private createFromForm(): IGame {
    return {
      ...new Game(),
      id: this.editForm.get(['id']).value,
      pointsA: this.editForm.get(['pointsA']).value,
      pointsB: this.editForm.get(['pointsB']).value,
      splitDeckNum: this.editForm.get(['splitDeckNum']).value,
      timeLeft: this.editForm.get(['timeLeft']).value,
      status: this.editForm.get(['status']).value,
      fixtureId: this.editForm.get(['fixtureId']).value,
      teamAId: this.editForm.get(['teamAId']).value,
      teamBId: this.editForm.get(['teamBId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>) {
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

  trackFixtureById(index: number, item: IFixture) {
    return item.id;
  }

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }
}
