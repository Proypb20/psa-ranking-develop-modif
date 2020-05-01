import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { BracketDetailComponent } from 'app/entities/bracket/bracket-detail.component';
import { Bracket } from 'app/shared/model/bracket.model';

describe('Component Tests', () => {
  describe('Bracket Management Detail Component', () => {
    let comp: BracketDetailComponent;
    let fixture: ComponentFixture<BracketDetailComponent>;
    const route = ({ data: of({ bracket: new Bracket(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [BracketDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BracketDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BracketDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bracket).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
