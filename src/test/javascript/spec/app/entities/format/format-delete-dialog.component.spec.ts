import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { FormatDeleteDialogComponent } from 'app/entities/format/format-delete-dialog.component';
import { FormatService } from 'app/entities/format/format.service';

describe('Component Tests', () => {
  describe('Format Management Delete Component', () => {
    let comp: FormatDeleteDialogComponent;
    let fixture: ComponentFixture<FormatDeleteDialogComponent>;
    let service: FormatService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [FormatDeleteDialogComponent]
      })
        .overrideTemplate(FormatDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormatDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormatService);
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
