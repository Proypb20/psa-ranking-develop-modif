import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IDocType, DocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from './doc-type.service';

@Component({
  selector: 'jhi-doc-type-update',
  templateUrl: './doc-type-update.component.html'
})
export class DocTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: []
  });

  constructor(protected docTypeService: DocTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ docType }) => {
      this.updateForm(docType);
    });
  }

  updateForm(docType: IDocType) {
    this.editForm.patchValue({
      id: docType.id,
      name: docType.name,
      description: docType.description
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const docType = this.createFromForm();
    if (docType.id !== undefined) {
      this.subscribeToSaveResponse(this.docTypeService.update(docType));
    } else {
      this.subscribeToSaveResponse(this.docTypeService.create(docType));
    }
  }

  private createFromForm(): IDocType {
    return {
      ...new DocType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocType>>) {
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
