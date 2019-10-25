import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IProvince, Province } from 'app/shared/model/province.model';
import { ProvinceService } from './province.service';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country/country.service';

@Component({
  selector: 'jhi-province-update',
  templateUrl: './province-update.component.html'
})
export class ProvinceUpdateComponent implements OnInit {
  isSaving: boolean;

  countries: ICountry[];

  editForm = this.fb.group({
    id: [],
    name: [],
    countryId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected provinceService: ProvinceService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ province }) => {
      this.updateForm(province);
    });
    this.countryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICountry[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICountry[]>) => response.body)
      )
      .subscribe((res: ICountry[]) => (this.countries = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(province: IProvince) {
    this.editForm.patchValue({
      id: province.id,
      name: province.name,
      countryId: province.countryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const province = this.createFromForm();
    if (province.id !== undefined) {
      this.subscribeToSaveResponse(this.provinceService.update(province));
    } else {
      this.subscribeToSaveResponse(this.provinceService.create(province));
    }
  }

  private createFromForm(): IProvince {
    return {
      ...new Province(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      countryId: this.editForm.get(['countryId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvince>>) {
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

  trackCountryById(index: number, item: ICountry) {
    return item.id;
  }
}
