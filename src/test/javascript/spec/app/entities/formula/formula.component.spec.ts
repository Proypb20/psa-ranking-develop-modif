import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PbPointsTestModule } from '../../../test.module';
import { FormulaComponent } from 'app/entities/formula/formula.component';
import { FormulaService } from 'app/entities/formula/formula.service';
import { Formula } from 'app/shared/model/formula.model';

describe('Component Tests', () => {
  describe('Formula Management Component', () => {
    let comp: FormulaComponent;
    let fixture: ComponentFixture<FormulaComponent>;
    let service: FormulaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [FormulaComponent],
        providers: []
      })
        .overrideTemplate(FormulaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormulaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormulaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Formula(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.formulas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
