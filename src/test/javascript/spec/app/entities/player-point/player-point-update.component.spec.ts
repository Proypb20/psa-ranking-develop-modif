import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { PlayerPointUpdateComponent } from 'app/entities/player-point/player-point-update.component';
import { PlayerPointService } from 'app/entities/player-point/player-point.service';
import { PlayerPoint } from 'app/shared/model/player-point.model';

describe('Component Tests', () => {
  describe('PlayerPoint Management Update Component', () => {
    let comp: PlayerPointUpdateComponent;
    let fixture: ComponentFixture<PlayerPointUpdateComponent>;
    let service: PlayerPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [PlayerPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlayerPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerPoint(123);
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
        const entity = new PlayerPoint();
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
