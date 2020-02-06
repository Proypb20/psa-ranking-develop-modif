import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsaRankingTestModule } from '../../../test.module';
import { PlayerDetailPointDeleteDialogComponent } from 'app/entities/player-detail-point/player-detail-point-delete-dialog.component';
import { PlayerDetailPointService } from 'app/entities/player-detail-point/player-detail-point.service';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Delete Component', () => {
    let comp: PlayerDetailPointDeleteDialogComponent;
    let fixture: ComponentFixture<PlayerDetailPointDeleteDialogComponent>;
    let service: PlayerDetailPointService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [PlayerDetailPointDeleteDialogComponent]
      })
        .overrideTemplate(PlayerDetailPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerDetailPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerDetailPointService);
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
