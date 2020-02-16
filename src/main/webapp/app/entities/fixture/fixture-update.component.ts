import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFixture, Fixture } from 'app/shared/model/fixture.model';
import { FixtureService } from './fixture.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

@Component({
  selector: 'jhi-fixture-update',
  templateUrl: './fixture-update.component.html'
})
export class FixtureUpdateComponent implements OnInit {
  isSaving: boolean;

  events: IEvent[];

  categories: ICategory[];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    eventId: [null, Validators.required],
    categoryId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected fixtureService: FixtureService,
    protected eventService: EventService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ fixture }) => {
      this.updateForm(fixture);
    });
    this.eventService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(fixture: IFixture) {
    this.editForm.patchValue({
      id: fixture.id,
      status: fixture.status,
      eventId: fixture.eventId,
      categoryId: fixture.categoryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const fixture = this.createFromForm();
    if (fixture.id !== undefined) {
      this.subscribeToSaveResponse(this.fixtureService.update(fixture));
    } else {
      this.subscribeToSaveResponse(this.fixtureService.create(fixture));
    }
  }

  private createFromForm(): IFixture {
    return {
      ...new Fixture(),
      id: this.editForm.get(['id']).value,
      status: this.editForm.get(['status']).value,
      eventId: this.editForm.get(['eventId']).value,
      categoryId: this.editForm.get(['categoryId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixture>>) {
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

  trackEventById(index: number, item: IEvent) {
    return item.id;
  }

  trackCategoryById(index: number, item: ICategory) {
    return item.id;
  }
}