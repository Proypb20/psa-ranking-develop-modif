import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { EventCategoryDeleteDialogComponent } from 'app/entities/event-category/event-category-delete-dialog.component';
import { EventCategoryService } from 'app/entities/event-category/event-category.service';

describe('Component Tests', () => {
  describe('EventCategory Management Delete Component', () => {
    let comp: EventCategoryDeleteDialogComponent;
    let fixture: ComponentFixture<EventCategoryDeleteDialogComponent>;
    let service: EventCategoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [EventCategoryDeleteDialogComponent]
      })
        .overrideTemplate(EventCategoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventCategoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventCategoryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
