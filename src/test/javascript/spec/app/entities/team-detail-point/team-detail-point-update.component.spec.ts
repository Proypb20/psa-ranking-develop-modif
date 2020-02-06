import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { TeamDetailPointUpdateComponent } from 'app/entities/team-detail-point/team-detail-point-update.component';
import { TeamDetailPointService } from 'app/entities/team-detail-point/team-detail-point.service';
import { TeamDetailPoint } from 'app/shared/model/team-detail-point.model';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Update Component', () => {
    let comp: TeamDetailPointUpdateComponent;
    let fixture: ComponentFixture<TeamDetailPointUpdateComponent>;
    let service: TeamDetailPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [TeamDetailPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TeamDetailPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamDetailPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeamDetailPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TeamDetailPoint(123);
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
        const entity = new TeamDetailPoint();
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
