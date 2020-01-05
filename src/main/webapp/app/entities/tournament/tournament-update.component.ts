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
import { ITournament, Tournament } from 'app/shared/model/tournament.model';
import { TournamentService } from './tournament.service';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';

@Component({
  selector: 'jhi-tournament-update',
  templateUrl: './tournament-update.component.html'
})
export class TournamentUpdateComponent implements OnInit {
  isSaving: boolean;

  userextras: IUserExtra[];

  editForm = this.fb.group({
    id: [],
    name: [],
    closeInscrDays: [],
    status: [],
    createDate: [],
    updatedDate: [],
    ownerId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tournamentService: TournamentService,
    protected userExtraService: UserExtraService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.updateForm(tournament);
    });
    this.userExtraService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserExtra[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserExtra[]>) => response.body)
      )
      .subscribe((res: IUserExtra[]) => (this.userextras = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tournament: ITournament) {
    this.editForm.patchValue({
      id: tournament.id,
      name: tournament.name,
      closeInscrDays: tournament.closeInscrDays,
      status: tournament.status,
      createDate: tournament.createDate != null ? tournament.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: tournament.updatedDate != null ? tournament.updatedDate.format(DATE_TIME_FORMAT) : null,
      ownerId: tournament.ownerId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tournament = this.createFromForm();
    if (tournament.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentService.update(tournament));
    } else {
      this.subscribeToSaveResponse(this.tournamentService.create(tournament));
    }
  }

  private createFromForm(): ITournament {
    return {
      ...new Tournament(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      closeInscrDays: this.editForm.get(['closeInscrDays']).value,
      status: this.editForm.get(['status']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      ownerId: this.editForm.get(['ownerId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournament>>) {
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
