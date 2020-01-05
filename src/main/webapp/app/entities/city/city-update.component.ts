import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICity, City } from 'app/shared/model/city.model';
import { CityService } from './city.service';
import { IProvince } from 'app/shared/model/province.model';
import { ProvinceService } from 'app/entities/province/province.service';

@Component({
  selector: 'jhi-city-update',
  templateUrl: './city-update.component.html'
})
export class CityUpdateComponent implements OnInit {
  isSaving: boolean;

  provinces: IProvince[];

  editForm = this.fb.group({
    id: [],
    name: [],
    latitude: [],
    longitude: [],
    provinceId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cityService: CityService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ city }) => {
      this.updateForm(city);
    });
    this.provinceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProvince[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProvince[]>) => response.body)
      )
      .subscribe((res: IProvince[]) => (this.provinces = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(city: ICity) {
    this.editForm.patchValue({
      id: city.id,
      name: city.name,
      latitude: city.latitude,
      longitude: city.longitude,
      provinceId: city.provinceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const city = this.createFromForm();
    if (city.id !== undefined) {
      this.subscribeToSaveResponse(this.cityService.update(city));
    } else {
      this.subscribeToSaveResponse(this.cityService.create(city));
    }
  }

  private createFromForm(): ICity {
    return {
      ...new City(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      latitude: this.editForm.get(['latitude']).value,
      longitude: this.editForm.get(['longitude']).value,
      provinceId: this.editForm.get(['provinceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICity>>) {
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

  trackProvinceById(index: number, item: IProvince) {
    return item.id;
  }
}
