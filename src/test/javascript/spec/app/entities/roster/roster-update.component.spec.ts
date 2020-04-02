import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { RosterUpdateComponent } from 'app/entities/roster/roster-update.component';
import { RosterService } from 'app/entities/roster/roster.service';
import { Roster } from 'app/shared/model/roster.model';

describe('Component Tests', () => {
  describe('Roster Management Update Component', () => {
    let comp: RosterUpdateComponent;
    let fixture: ComponentFixture<RosterUpdateComponent>;
    let service: RosterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [RosterUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RosterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RosterUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RosterService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Roster(123);
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
        const entity = new Roster();
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
