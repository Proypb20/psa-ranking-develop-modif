import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { TeamPointDeleteDialogComponent } from 'app/entities/team-point/team-point-delete-dialog.component';
import { TeamPointService } from 'app/entities/team-point/team-point.service';

describe('Component Tests', () => {
  describe('TeamPoint Management Delete Component', () => {
    let comp: TeamPointDeleteDialogComponent;
    let fixture: ComponentFixture<TeamPointDeleteDialogComponent>;
    let service: TeamPointService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [TeamPointDeleteDialogComponent]
      })
        .overrideTemplate(TeamPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeamPointService);
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
