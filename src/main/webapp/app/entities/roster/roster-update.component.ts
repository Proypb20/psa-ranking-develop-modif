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
import { IRoster, Roster } from 'app/shared/model/roster.model';
import { RosterService } from './roster.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-roster-update',
  templateUrl: './roster-update.component.html'
})
export class RosterUpdateComponent implements OnInit {
  isSaving: boolean;

  categories: ICategory[];

  players: IPlayer[];

  editForm = this.fb.group({
    id: [],
    active: [],
    profile: [],
    createDate: [],
    updatedDate: [],
    categoryId: [],
    players: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rosterService: RosterService,
    protected categoryService: CategoryService,
    protected playerService: PlayerService,
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
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(roster: IRoster) {
    this.editForm.patchValue({
      id: roster.id,
      active: roster.active,
      profile: roster.profile,
      createDate: roster.createDate != null ? roster.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: roster.updatedDate != null ? roster.updatedDate.format(DATE_TIME_FORMAT) : null,
      categoryId: roster.categoryId,
      players: roster.players
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
      profile: this.editForm.get(['profile']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      categoryId: this.editForm.get(['categoryId']).value,
      players: this.editForm.get(['players']).value
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
}
