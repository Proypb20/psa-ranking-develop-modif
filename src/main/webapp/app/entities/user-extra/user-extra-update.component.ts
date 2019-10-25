import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IUserExtra, UserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from './user-extra.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IDocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/doc-type.service';

@Component({
  selector: 'jhi-user-extra-update',
  templateUrl: './user-extra-update.component.html'
})
export class UserExtraUpdateComponent implements OnInit {
  isSaving: boolean;
  users: IUser[];
  doctypes: IDocType[];
  bornDateDp: any;

  editForm = this.fb.group({
    id: [],
    numDoc: [],
    phone: [],
    bornDate: [],
    userId: [],
    docTypeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userExtraService: UserExtraService,
    protected userService: UserService,
    protected docTypeService: DocTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userExtra }) => {
      this.updateForm(userExtra);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.docTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDocType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDocType[]>) => response.body)
      )
      .subscribe((res: IDocType[]) => (this.doctypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userExtra: IUserExtra) {
    this.editForm.patchValue({
      id: userExtra.id,
      numDoc: userExtra.numDoc,
      phone: userExtra.phone,
      bornDate: userExtra.bornDate,
      userId: userExtra.userId,
      docTypeId: userExtra.docTypeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userExtra = this.createFromForm();
    if (userExtra.id !== undefined) {
      this.subscribeToSaveResponse(this.userExtraService.update(userExtra));
    } else {
      this.subscribeToSaveResponse(this.userExtraService.create(userExtra));
    }
  }

  private createFromForm(): IUserExtra {
    return {
      ...new UserExtra(),
      id: this.editForm.get(['id']).value,
      numDoc: this.editForm.get(['numDoc']).value,
      phone: this.editForm.get(['phone']).value,
      bornDate: this.editForm.get(['bornDate']).value,
      userId: this.editForm.get(['userId']).value,
      docTypeId: this.editForm.get(['docTypeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserExtra>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackDocTypeById(index: number, item: IDocType) {
    return item.id;
  }
}
