import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICategory, Category } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html'
})
export class CategoryUpdateComponent implements OnInit {
  isSaving: boolean;
  currentAccount: any;
  tournaments: ITournament[];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    gameTimeType: [null, [Validators.required]],
    gameTime: [null, [Validators.required]],
    stopTimeType: [null, [Validators.required]],
    stopTime: [null, [Validators.required]],
    totalPoints: [null, [Validators.required]],
    difPoints: [null, [Validators.required]],
    order: [null, [Validators.required]],
    tournamentId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected categoryService: CategoryService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);
    });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    if (this.currentAccount.authorities.includes('ROLE_ADMIN'))
    {
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    }
    else
    {
      this.tournamentService
	      .query({
	      'id.equals': +localStorage.getItem("TOURNAMENTID")
	      })
	      .pipe(
	        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
	        map((response: HttpResponse<ITournament[]>) => response.body)
	      )
	      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    }
  }

  updateForm(category: ICategory) {
    this.editForm.patchValue({
      id: category.id,
      name: category.name,
      description: category.description,
      gameTimeType: category.gameTimeType,
      gameTime: category.gameTime,
      stopTimeType: category.stopTimeType,
      stopTime: category.stopTime,
      totalPoints: category.totalPoints,
      difPoints: category.difPoints,
      order: category.order,
      tournamentId: category.tournamentId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  private createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      gameTimeType: this.editForm.get(['gameTimeType']).value,
      gameTime: this.editForm.get(['gameTime']).value,
      stopTimeType: this.editForm.get(['stopTimeType']).value,
      stopTime: this.editForm.get(['stopTime']).value,
      totalPoints: this.editForm.get(['totalPoints']).value,
      difPoints: this.editForm.get(['difPoints']).value,
      order: this.editForm.get(['order']).value,
      tournamentId: this.editForm.get(['tournamentId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>) {
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
}
