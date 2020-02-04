import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
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
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';

@Component({
  selector: 'jhi-roster-update',
  templateUrl: './roster-update.component.html'
})
export class RosterUpdateComponent implements OnInit {
  isSaving: boolean;

  categories: ICategory[];

  teams: ITeam[];

  editForm = this.fb.group({
    id: [],
    active: [],
    categoryId: [],
    teamId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rosterService: RosterService,
    protected categoryService: CategoryService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.updateForm(roster);
    });
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.teamService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITeam[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITeam[]>) => response.body)
      )
      .subscribe((res: ITeam[]) => (this.teams = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(roster: IRoster) {
    this.editForm.patchValue({
      id: roster.id,
      active: roster.active,
      categoryId: roster.categoryId,
      teamId: roster.teamId
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
      categoryId: this.editForm.get(['categoryId']).value,
      teamId: this.editForm.get(['teamId']).value
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

  trackTeamById(index: number, item: ITeam) {
    return item.id;
  }
}
