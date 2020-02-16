import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { FixtureDeleteDialogComponent } from 'app/entities/fixture/fixture-delete-dialog.component';
import { FixtureService } from 'app/entities/fixture/fixture.service';

describe('Component Tests', () => {
  describe('Fixture Management Delete Component', () => {
    let comp: FixtureDeleteDialogComponent;
    let fixture: ComponentFixture<FixtureDeleteDialogComponent>;
    let service: FixtureService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [FixtureDeleteDialogComponent]
      })
        .overrideTemplate(FixtureDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixtureDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixtureService);
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
