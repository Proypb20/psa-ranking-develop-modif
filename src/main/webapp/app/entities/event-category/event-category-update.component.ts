import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEventCategory, EventCategory } from 'app/shared/model/event-category.model';
import { EventCategoryService } from './event-category.service';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event/event.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IFormat } from 'app/shared/model/format.model';
import { FormatService } from 'app/entities/format/format.service';

@Component({
  selector: 'jhi-event-category-update',
  templateUrl: './event-category-update.component.html'
})
export class EventCategoryUpdateComponent implements OnInit {
  isSaving: boolean;

  events: IEvent[];

  categories: ICategory[];

  formats: IFormat[];

  editForm = this.fb.group({
    id: [],
    splitDeck: [],
    eventId: [null, Validators.required],
    categoryId: [null, Validators.required],
    formatId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected eventCategoryService: EventCategoryService,
    protected eventService: EventService,
    protected categoryService: CategoryService,
    protected formatService: FormatService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ eventCategory }) => {
      this.updateForm(eventCategory);
    });
    this.eventService
      .query({
      'tournamentId.equals': +localStorage.getItem("TOURNAMENTID")
      })
      .pipe(
        filter((mayBeOk: HttpResponse<IEvent[]>) => mayBeOk.ok),
        map((response: HttpResponse<IEvent[]>) => response.body)
      )
      .subscribe((res: IEvent[]) => (this.events = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.categoryService
      .query({
      'tournamentId.equals': +localStorage.getItem("TOURNAMENTID")
      })
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.formatService
      .query({
      'tournamentId.equals': +localStorage.getItem("TOURNAMENTID")
      })
      .pipe(
        filter((mayBeOk: HttpResponse<IFormat[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFormat[]>) => response.body)
      )
      .subscribe((res: IFormat[]) => (this.formats = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(eventCategory: IEventCategory) {
    this.editForm.patchValue({
      id: eventCategory.id,
      splitDeck: eventCategory.splitDeck,
      eventId: eventCategory.eventId,
      categoryId: eventCategory.categoryId,
      formatId: eventCategory.formatId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const eventCategory = this.createFromForm();
    if (eventCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.eventCategoryService.update(eventCategory));
    } else {
      this.subscribeToSaveResponse(this.eventCategoryService.create(eventCategory));
    }
  }

  private createFromForm(): IEventCategory {
    return {
      ...new EventCategory(),
      id: this.editForm.get(['id']).value,
      splitDeck: this.editForm.get(['splitDeck']).value,
      eventId: this.editForm.get(['eventId']).value,
      categoryId: this.editForm.get(['categoryId']).value,
      formatId: this.editForm.get(['formatId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventCategory>>) {
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

  trackFormatById(index: number, item: IFormat) {
    return item.id;
  }
}
