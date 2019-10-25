import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { RosterDeleteDialogComponent } from 'app/entities/roster/roster-delete-dialog.component';
import { RosterService } from 'app/entities/roster/roster.service';

describe('Component Tests', () => {
  describe('Roster Management Delete Component', () => {
    let comp: RosterDeleteDialogComponent;
    let fixture: ComponentFixture<RosterDeleteDialogComponent>;
    let service: RosterService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [RosterDeleteDialogComponent]
      })
        .overrideTemplate(RosterDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RosterDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RosterService);
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
