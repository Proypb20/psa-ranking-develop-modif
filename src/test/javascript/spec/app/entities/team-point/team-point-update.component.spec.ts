import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { TeamPointUpdateComponent } from 'app/entities/team-point/team-point-update.component';
import { TeamPointService } from 'app/entities/team-point/team-point.service';
import { TeamPoint } from 'app/shared/model/team-point.model';

describe('Component Tests', () => {
  describe('TeamPoint Management Update Component', () => {
    let comp: TeamPointUpdateComponent;
    let fixture: ComponentFixture<TeamPointUpdateComponent>;
    let service: TeamPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [TeamPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TeamPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeamPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TeamPoint(123);
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
        const entity = new TeamPoint();
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
