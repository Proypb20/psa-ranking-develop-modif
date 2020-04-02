import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PbPointsTestModule } from '../../../test.module';
import { PlayerPointDeleteDialogComponent } from 'app/entities/player-point/player-point-delete-dialog.component';
import { PlayerPointService } from 'app/entities/player-point/player-point.service';

describe('Component Tests', () => {
  describe('PlayerPoint Management Delete Component', () => {
    let comp: PlayerPointDeleteDialogComponent;
    let fixture: ComponentFixture<PlayerPointDeleteDialogComponent>;
    let service: PlayerPointService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [PlayerPointDeleteDialogComponent]
      })
        .overrideTemplate(PlayerPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerPointService);
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
