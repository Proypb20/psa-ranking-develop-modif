import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PbPointsTestModule } from '../../../test.module';
import { DocTypeDeleteDialogComponent } from 'app/entities/doc-type/doc-type-delete-dialog.component';
import { DocTypeService } from 'app/entities/doc-type/doc-type.service';

describe('Component Tests', () => {
  describe('DocType Management Delete Component', () => {
    let comp: DocTypeDeleteDialogComponent;
    let fixture: ComponentFixture<DocTypeDeleteDialogComponent>;
    let service: DocTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [DocTypeDeleteDialogComponent]
      })
        .overrideTemplate(DocTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocTypeService);
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
