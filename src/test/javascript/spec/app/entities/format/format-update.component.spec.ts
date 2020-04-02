import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { FormatUpdateComponent } from 'app/entities/format/format-update.component';
import { FormatService } from 'app/entities/format/format.service';
import { Format } from 'app/shared/model/format.model';

describe('Component Tests', () => {
  describe('Format Management Update Component', () => {
    let comp: FormatUpdateComponent;
    let fixture: ComponentFixture<FormatUpdateComponent>;
    let service: FormatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [FormatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FormatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Format(123);
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
        const entity = new Format();
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
