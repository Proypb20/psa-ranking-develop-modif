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
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { IDocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from 'app/entities/doc-type/doc-type.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving: boolean;

  cities: ICity[];

  doctypes: IDocType[];

  editForm = this.fb.group({
    id: [],
    psaId: [],
    eraseDate: [],
    active: [],
    createDate: [],
    updatedDate: [],
    address: [],
    zipCode: [],
    cityId: [],
    docTypeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected personService: PersonService,
    protected cityService: CityService,
    protected docTypeService: DocTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);
    });
    this.cityService
      .query({ filter: 'person-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe(
        (res: ICity[]) => {
          if (!this.editForm.get('cityId').value) {
            this.cities = res;
          } else {
            this.cityService
              .find(this.editForm.get('cityId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<ICity>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<ICity>) => subResponse.body)
              )
              .subscribe(
                (subRes: ICity) => (this.cities = [subRes].concat(res)),
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
      psaId: person.psaId,
      eraseDate: person.eraseDate != null ? person.eraseDate.format(DATE_TIME_FORMAT) : null,
      active: person.active,
      createDate: person.createDate != null ? person.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: person.updatedDate != null ? person.updatedDate.format(DATE_TIME_FORMAT) : null,
      address: person.address,
      zipCode: person.zipCode,
      cityId: person.cityId,
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
      psaId: this.editForm.get(['psaId']).value,
      eraseDate:
        this.editForm.get(['eraseDate']).value != null ? moment(this.editForm.get(['eraseDate']).value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      address: this.editForm.get(['address']).value,
      zipCode: this.editForm.get(['zipCode']).value,
      cityId: this.editForm.get(['cityId']).value,
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

  trackCityById(index: number, item: ICity) {
    return item.id;
  }

  trackDocTypeById(index: number, item: IDocType) {
    return item.id;
  }
}
