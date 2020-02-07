import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventCategory } from 'app/shared/model/event-category.model';

@Component({
  selector: 'jhi-event-category-detail',
  templateUrl: './event-category-detail.component.html'
})
export class EventCategoryDetailComponent implements OnInit {
  eventCategory: IEventCategory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ eventCategory }) => {
      this.eventCategory = eventCategory;
    });
  }

  previousState() {
    window.history.back();
  }
}
