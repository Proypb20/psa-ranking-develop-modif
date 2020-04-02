import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { PlayerDetailPointUpdateComponent } from 'app/entities/player-detail-point/player-detail-point-update.component';
import { PlayerDetailPointService } from 'app/entities/player-detail-point/player-detail-point.service';
import { PlayerDetailPoint } from 'app/shared/model/player-detail-point.model';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Update Component', () => {
    let comp: PlayerDetailPointUpdateComponent;
    let fixture: ComponentFixture<PlayerDetailPointUpdateComponent>;
    let service: PlayerDetailPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [PlayerDetailPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlayerDetailPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerDetailPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerDetailPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerDetailPoint(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerDetailPoint();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
