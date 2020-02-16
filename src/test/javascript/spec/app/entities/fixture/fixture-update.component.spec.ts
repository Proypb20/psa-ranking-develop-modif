import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { FixtureUpdateComponent } from 'app/entities/fixture/fixture-update.component';
import { FixtureService } from 'app/entities/fixture/fixture.service';
import { Fixture } from 'app/shared/model/fixture.model';

describe('Component Tests', () => {
  describe('Fixture Management Update Component', () => {
    let comp: FixtureUpdateComponent;
    let fixture: ComponentFixture<FixtureUpdateComponent>;
    let service: FixtureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [FixtureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FixtureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixtureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixtureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Fixture(123);
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
        const entity = new Fixture();
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
