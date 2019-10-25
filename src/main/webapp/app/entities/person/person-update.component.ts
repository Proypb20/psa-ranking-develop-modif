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
import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address/address.service';
import { IDocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/doc-type.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving: boolean;

  addresses: IAddress[];

  doctypes: IDocType[];

  editForm = this.fb.group({
    id: [],
    names: [],
    surnames: [],
    email: [],
    phone: [],
    bornDate: [],
    psaId: [],
    eraseDate: [],
    active: [],
    createDate: [],
    updatedDate: [],
    addressId: [],
    docTypeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected personService: PersonService,
    protected addressService: AddressService,
    protected docTypeService: DocTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);
    });
    this.addressService
      .query({ filter: 'person-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAddress[]>) => response.body)
      )
      .subscribe(
        (res: IAddress[]) => {
          if (!this.editForm.get('addressId').value) {
            this.addresses = res;
          } else {
            this.addressService
              .find(this.editForm.get('addressId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IAddress>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IAddress>) => subResponse.body)
              )
              .subscribe(
                (subRes: IAddress) => (this.addresses = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.docTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDocType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDocType[]>) => response.body)
      )
      .subscribe((res: IDocType[]) => (this.doctypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(person: IPerson) {
    this.editForm.patchValue({
      id: person.id,
      names: person.names,
      surnames: person.surnames,
      email: person.email,
      phone: person.phone,
      bornDate: person.bornDate != null ? person.bornDate.format(DATE_TIME_FORMAT) : null,
      psaId: person.psaId,
      eraseDate: person.eraseDate != null ? person.eraseDate.format(DATE_TIME_FORMAT) : null,
      active: person.active,
      createDate: person.createDate != null ? person.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: person.updatedDate != null ? person.updatedDate.format(DATE_TIME_FORMAT) : null,
      addressId: person.addressId,
      docTypeId: person.docTypeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id']).value,
      names: this.editForm.get(['names']).value,
      surnames: this.editForm.get(['surnames']).value,
      email: this.editForm.get(['email']).value,
      phone: this.editForm.get(['phone']).value,
      bornDate: this.editForm.get(['bornDate']).value != null ? moment(this.editForm.get(['bornDate']).value, DATE_TIME_FORMAT) : undefined,
      psaId: this.editForm.get(['psaId']).value,
      eraseDate:
        this.editForm.get(['eraseDate']).value != null ? moment(this.editForm.get(['eraseDate']).value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      addressId: this.editForm.get(['addressId']).value,
      docTypeId: this.editForm.get(['docTypeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>) {
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

  trackAddressById(index: number, item: IAddress) {
    return item.id;
  }

  trackDocTypeById(index: number, item: IDocType) {
    return item.id;
  }
}
