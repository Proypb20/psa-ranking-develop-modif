import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PbPointsTestModule } from '../../../test.module';
import { TeamDetailPointDeleteDialogComponent } from 'app/entities/team-detail-point/team-detail-point-delete-dialog.component';
import { TeamDetailPointService } from 'app/entities/team-detail-point/team-detail-point.service';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Delete Component', () => {
    let comp: TeamDetailPointDeleteDialogComponent;
    let fixture: ComponentFixture<TeamDetailPointDeleteDialogComponent>;
    let service: TeamDetailPointService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [TeamDetailPointDeleteDialogComponent]
      })
        .overrideTemplate(TeamDetailPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamDetailPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeamDetailPointService);
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
