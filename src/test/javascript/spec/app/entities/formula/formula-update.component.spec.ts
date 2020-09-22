import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { FormulaUpdateComponent } from 'app/entities/formula/formula-update.component';
import { FormulaService } from 'app/entities/formula/formula.service';
import { Formula } from 'app/shared/model/formula.model';

describe('Component Tests', () => {
  describe('Formula Management Update Component', () => {
    let comp: FormulaUpdateComponent;
    let fixture: ComponentFixture<FormulaUpdateComponent>;
    let service: FormulaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [FormulaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FormulaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormulaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormulaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Formula(123);
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
        const entity = new Formula();
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
