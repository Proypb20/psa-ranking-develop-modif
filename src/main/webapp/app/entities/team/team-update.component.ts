import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITeam, Team } from 'app/shared/model/team.model';
import { TeamService } from './team.service';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';

@Component({
  selector: 'jhi-team-update',
  templateUrl: './team-update.component.html'
})
export class TeamUpdateComponent implements OnInit {
  isSaving: boolean;

  userextras: IUserExtra[];

  editForm = this.fb.group({
    id: [],
    name: [],
    active: [],
    createDate: [],
    updatedDate: [],
    ownerId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected teamService: TeamService,
    protected userExtraService: UserExtraService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ team }) => {
      this.updateForm(team);
    });
    this.userExtraService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserExtra[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserExtra[]>) => response.body)
      )
      .subscribe((res: IUserExtra[]) => (this.userextras = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(team: ITeam) {
    this.editForm.patchValue({
      id: team.id,
      name: team.name,
      active: team.active,
      createDate: team.createDate != null ? team.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: team.updatedDate != null ? team.updatedDate.format(DATE_TIME_FORMAT) : null,
      ownerId: team.ownerId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const team = this.createFromForm();
    if (team.id !== undefined) {
      this.subscribeToSaveResponse(this.teamService.update(team));
    } else {
      this.subscribeToSaveResponse(this.teamService.create(team));
    }
  }

  private createFromForm(): ITeam {
    return {
      ...new Team(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      active: this.editForm.get(['active']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      ownerId: this.editForm.get(['ownerId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>) {
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

  trackUserExtraById(index: number, item: IUserExtra) {
    return item.id;
  }
}
