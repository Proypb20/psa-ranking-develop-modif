import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { FormulaDetailComponent } from 'app/entities/formula/formula-detail.component';
import { Formula } from 'app/shared/model/formula.model';

describe('Component Tests', () => {
  describe('Formula Management Detail Component', () => {
    let comp: FormulaDetailComponent;
    let fixture: ComponentFixture<FormulaDetailComponent>;
    const route = ({ data: of({ formula: new Formula(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [FormulaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FormulaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormulaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formula).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
