import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpHeaders, HttpResponse, HttpErrorResponse} from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRoster, Roster } from 'app/shared/model/roster.model';
import { RosterService } from './roster.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';

@Component({
  selector: 'jhi-roster-update',
  templateUrl: './roster-update.component.html'
})
export class RosterUpdateComponent implements OnInit {
  isSaving: boolean;

  categories: ICategory[];

  players: IPlayer[];

  teams: ITeam[];

  tournaments: ITournament[];

  events: IEvent[];
  
  tId: number;
  teId: number;
  cId: number;
  evId: number;
  private sub: any;

  editForm = this.fb.group({
    id: [],
    active: [],
    categoryId: [],
    players: [],
    teamId: [null, Validators.required],
    tournamentId: [],
    eventId: []
    
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rosterService: RosterService,
    protected categoryService: CategoryService,
    protected playerService: PlayerService,
    protected teamService: TeamService,
    protected tournamentService: TournamentService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.sub = this.activatedRoute
        .queryParams
        .subscribe(params => {
           this.tId = +params['tId'] || 0;
           this.teId = +params['teId'] || 0;
           this.evId = +params['evId'] || 0;
           this.cId = +params['cId'] || 0;
      });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.updateForm(roster);
    });
    this.categoryService
      .query({'categoryId.equals': this.cId})
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  this.teamService
      .query({'teamId.equals': this.teId})
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tournamentService
      .query({'tournamentId.equals': this.tId,'status.equals':"CREATED"})
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.eventService
      .query({'eventId.equals': this.evId})
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(roster: IRoster) {
    this.editForm.patchValue({
      id: roster.id,
      active: roster.active,
      categoryId: this.cId || roster.categoryId,
      players: roster.players,
      teamId: this.teId || roster.teamId,
      tournamentId: this.tId || roster.tournamentId,
      eventId: this.evId || roster.eventId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const roster = this.createFromForm();
    if (roster.id !== undefined) {
      this.subscribeToSaveResponse(this.rosterService.update(roster));
    } else {
      this.subscribeToSaveResponse(this.rosterService.create(roster));
    }
  }

  private createFromForm(): IRoster {
    return {
      ...new Roster(),
      id: this.editForm.get(['id']).value,
      active: this.editForm.get(['active']).value,
      categoryId: this.cId ||this.editForm.get(['categoryId']).value,
      teamId: this.teId || this.editForm.get(['teamId']).value,
      tournamentId: this.tId ||this.editForm.get(['tournamentId']).value,
      eventId: this.evId || this.editForm.get(['eventId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoster>>) {
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

  trackCategoryById(index: number, item: ICategory) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
  
  getTournamentIdFromEventId(selectedVals: IEvent[], id: number) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (id === selectedVals[i].tournamentId) {
          return selectedVals[i].tournamentId;
        }
      }
  }

  trackTournamentById(index: number, item: ITournament) {
    return item.id;
  }

  trackEventById(index: number, item: IEvent) {
    return item.id;
  }
  
  onTournamentSelect()
  {
     this.categoryService
      .query({'tournamentId.equals': this.editForm.get(['tournamentId']).value})
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
     this.eventService
      .query({'tournamentId.equals': this.editForm.get(['tournamentId']).value})
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
  }
  
}
