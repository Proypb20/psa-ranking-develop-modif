import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { DocTypeUpdateComponent } from 'app/entities/doc-type/doc-type-update.component';
import { DocTypeService } from 'app/entities/doc-type/doc-type.service';
import { DocType } from 'app/shared/model/doc-type.model';

describe('Component Tests', () => {
  describe('DocType Management Update Component', () => {
    let comp: DocTypeUpdateComponent;
    let fixture: ComponentFixture<DocTypeUpdateComponent>;
    let service: DocTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [DocTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocType(123);
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
        const entity = new DocType();
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
