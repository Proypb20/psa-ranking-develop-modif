import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRoster, Roster } from 'app/shared/model/roster.model';
import { RosterService } from './roster.service';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';
import { IEventCategory } from 'app/shared/model/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/event-category.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-roster-update',
  templateUrl: './roster-update.component.html'
})
export class RosterUpdateComponent implements OnInit {
  isSaving: boolean;
  currentAccount: any;
  teams: ITeam[];
  
  teId: number;
  evCatId: number;
  private sub: any;

  eventcategories: IEventCategory[];

  editForm = this.fb.group({
    id: [],
    active: [],
    teamId: [null, Validators.required],
    eventCategoryId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rosterService: RosterService,
    protected teamService: TeamService,
    protected eventCategoryService: EventCategoryService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.sub = this.activatedRoute
        .queryParams
        .subscribe(params => {
           this.teId = +params['teId'] || 0;
           this.evCatId = +params['evId'] || 0;
      });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.updateForm(roster);
    });
      if (this.currentAccount.authorities.includes('ROLE_ADMIN'))
      {      
		  this.teamService
		      .query({"teamId.equals": this.teId
		            })
		      .pipe(
		        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
		        map((response: HttpResponse<ITeam[]>) => response.body)
		      )
		      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
	  }
	  else
      {
		  this.teamService
	      .query({"teamId.equals": this.teId,
	             "ownerId.equals": this.currentAccount.id
	            })
	      .pipe(
	            filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
	            map((response: HttpResponse<ITeam[]>) => response.body))
	      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
		      }
    if (this.evCatId)
    {
    this.eventCategoryService
      .query({"eventCategoryId.equals": this.evCatId})
      .pipe(
        filter((mayBeOk: HttpResponse<IEventCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEventCategory[]>) => response.body)
      )
      .subscribe((res: IEventCategory[]) => (this.eventcategories = res), (res: HttpErrorResponse) => this.onError(res.message));
      }
      else
      {
      this.eventCategoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IEventCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEventCategory[]>) => response.body)
      )
      .subscribe((res: IEventCategory[]) => (this.eventcategories = res), (res: HttpErrorResponse) => this.onError(res.message));
      }
  }

  updateForm(roster: IRoster) {
    this.editForm.patchValue({
      id: roster.id,
      active: roster.active,
      teamId: roster.teamId,
      eventCategoryId: roster.eventCategoryId
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
      teamId: this.teId || this.editForm.get(['teamId']).value,
      eventCategoryId: this.evCatId || this.editForm.get(['eventCategoryId']).value
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

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }

  trackEventCategoryById(index: number, item: IEventCategory) {
    return item.id;
  }
}
