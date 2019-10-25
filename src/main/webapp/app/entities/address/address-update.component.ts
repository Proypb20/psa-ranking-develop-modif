import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAddress, Address } from 'app/shared/model/address.model';
import { AddressService } from './address.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html'
})
export class AddressUpdateComponent implements OnInit {
  isSaving: boolean;

  cities: ICity[];

  userextras: IUserExtra[];

  editForm = this.fb.group({
    id: [],
    street: [],
    zipCode: [],
    cityId: [],
    userExtraId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected addressService: AddressService,
    protected cityService: CityService,
    protected userExtraService: UserExtraService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ address }) => {
      this.updateForm(address);
    });
    this.cityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userExtraService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserExtra[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserExtra[]>) => response.body)
      )
      .subscribe((res: IUserExtra[]) => (this.userextras = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(address: IAddress) {
    this.editForm.patchValue({
      id: address.id,
      street: address.street,
      zipCode: address.zipCode,
      cityId: address.cityId,
      userExtraId: address.userExtraId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const address = this.createFromForm();
    if (address.id !== undefined) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  private createFromForm(): IAddress {
    return {
      ...new Address(),
      id: this.editForm.get(['id']).value,
      street: this.editForm.get(['street']).value,
      zipCode: this.editForm.get(['zipCode']).value,
      cityId: this.editForm.get(['cityId']).value,
      userExtraId: this.editForm.get(['userExtraId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>) {
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

  trackUserExtraById(index: number, item: IUserExtra) {
    return item.id;
  }
}
