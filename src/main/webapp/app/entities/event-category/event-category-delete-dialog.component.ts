import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEventCategory } from 'app/shared/model/event-category.model';
import { EventCategoryService } from './event-category.service';

@Component({
  selector: 'jhi-event-category-delete-dialog',
  templateUrl: './event-category-delete-dialog.component.html'
})
export class EventCategoryDeleteDialogComponent {
  eventCategory: IEventCategory;

  constructor(
    protected eventCategoryService: EventCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.eventCategoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'eventCategoryListModification',
        content: 'Deleted an eventCategory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-event-category-delete-popup',
  template: ''
})
export class EventCategoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ eventCategory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EventCategoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.eventCategory = eventCategory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/event-category', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/event-category', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
