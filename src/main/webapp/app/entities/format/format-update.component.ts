import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFormat, Format } from 'app/shared/model/format.model';
import { FormatService } from './format.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';

@Component({
  selector: 'jhi-format-update',
  templateUrl: './format-update.component.html'
})
export class FormatUpdateComponent implements OnInit {
  isSaving: boolean;

  tournaments: ITournament[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    coeficient: [null, [Validators.required]],
    playersQty: [],
    tournamentId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected formatService: FormatService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ format }) => {
      this.updateForm(format);
    });
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(format: IFormat) {
    this.editForm.patchValue({
      id: format.id,
      name: format.name,
      description: format.description,
      coeficient: format.coeficient,
      playersQty: format.playersQty,
      tournamentId: format.tournamentId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const format = this.createFromForm();
    if (format.id !== undefined) {
      this.subscribeToSaveResponse(this.formatService.update(format));
    } else {
      this.subscribeToSaveResponse(this.formatService.create(format));
    }
  }

  private createFromForm(): IFormat {
    return {
      ...new Format(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      coeficient: this.editForm.get(['coeficient']).value,
      playersQty: this.editForm.get(['playersQty']).value,
      tournamentId: this.editForm.get(['tournamentId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormat>>) {
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
	