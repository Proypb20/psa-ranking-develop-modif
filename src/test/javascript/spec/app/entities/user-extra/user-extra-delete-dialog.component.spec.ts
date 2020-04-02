import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PbPointsTestModule } from '../../../test.module';
import { UserExtraDeleteDialogComponent } from 'app/entities/user-extra/user-extra-delete-dialog.component';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';

describe('Component Tests', () => {
  describe('UserExtra Management Delete Component', () => {
    let comp: UserExtraDeleteDialogComponent;
    let fixture: ComponentFixture<UserExtraDeleteDialogComponent>;
    let service: UserExtraService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [UserExtraDeleteDialogComponent]
      })
        .overrideTemplate(UserExtraDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserExtraDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserExtraService);
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
