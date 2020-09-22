import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PbPointsTestModule } from '../../../test.module';
import { FormulaDeleteDialogComponent } from 'app/entities/formula/formula-delete-dialog.component';
import { FormulaService } from 'app/entities/formula/formula.service';

describe('Component Tests', () => {
  describe('Formula Management Delete Component', () => {
    let comp: FormulaDeleteDialogComponent;
    let fixture: ComponentFixture<FormulaDeleteDialogComponent>;
    let service: FormulaService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [FormulaDeleteDialogComponent]
      })
        .overrideTemplate(FormulaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormulaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormulaService);
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
