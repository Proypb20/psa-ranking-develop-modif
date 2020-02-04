import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IFormat, Format } from 'app/shared/model/format.model';
import { FormatService } from './format.service';

@Component({
  selector: 'jhi-format-update',
  templateUrl: './format-update.component.html'
})
export class FormatUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    coeficient: [null, [Validators.required]]
  });

  constructor(protected formatService: FormatService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ format }) => {
      this.updateForm(format);
    });
  }

  updateForm(format: IFormat) {
    this.editForm.patchValue({
      id: format.id,
      name: format.name,
      description: format.description,
      coeficient: format.coeficient
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
      coeficient: this.editForm.get(['coeficient']).value
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
}
