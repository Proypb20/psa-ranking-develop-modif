import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { EventCategoryUpdateComponent } from 'app/entities/event-category/event-category-update.component';
import { EventCategoryService } from 'app/entities/event-category/event-category.service';
import { EventCategory } from 'app/shared/model/event-category.model';

describe('Component Tests', () => {
  describe('EventCategory Management Update Component', () => {
    let comp: EventCategoryUpdateComponent;
    let fixture: ComponentFixture<EventCategoryUpdateComponent>;
    let service: EventCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [EventCategoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EventCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventCategory(123);
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
        const entity = new EventCategory();
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
