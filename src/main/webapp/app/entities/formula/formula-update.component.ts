import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFormula, Formula } from 'app/shared/model/formula.model';
import { FormulaService } from './formula.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';

@Component({
  selector: 'jhi-formula-update',
  templateUrl: './formula-update.component.html'
})
export class FormulaUpdateComponent implements OnInit {
  isSaving: boolean;

  tournaments: ITournament[];

  editForm = this.fb.group({
    id: [],
    formula: [null, [Validators.required]],
    var1: [null, [Validators.required]],
    var2: [],
    var3: [],
    description: [null, [Validators.required]],
    example: [],
    tournamentId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected formulaService: FormulaService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ formula }) => {
      this.updateForm(formula);
    });
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(formula: IFormula) {
    this.editForm.patchValue({
      id: formula.id,
      formula: formula.formula,
      var1: formula.var1,
      var2: formula.var2,
      var3: formula.var3,
      description: formula.description,
      example: formula.example,
      tournamentId: formula.tournamentId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const formula = this.createFromForm();
    if (formula.id !== undefined) {
      this.subscribeToSaveResponse(this.formulaService.update(formula));
    } else {
      this.subscribeToSaveResponse(this.formulaService.create(formula));
    }
  }

  private createFromForm(): IFormula {
    return {
      ...new Formula(),
      id: this.editForm.get(['id']).value,
      formula: this.editForm.get(['formula']).value,
      var1: this.editForm.get(['var1']).value,
      var2: this.editForm.get(['var2']).value,
      var3: this.editForm.get(['var3']).value,
      description: this.editForm.get(['description']).value,
      example: this.editForm.get(['example']).value,
      tournamentId: this.editForm.get(['tournamentId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormula>>) {
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
