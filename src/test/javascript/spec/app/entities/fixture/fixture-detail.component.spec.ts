import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { FixtureDetailComponent } from 'app/entities/fixture/fixture-detail.component';
import { Fixture } from 'app/shared/model/fixture.model';

describe('Component Tests', () => {
  describe('Fixture Management Detail Component', () => {
    let comp: FixtureDetailComponent;
    let fixture: ComponentFixture<FixtureDetailComponent>;
    const route = ({ data: of({ fixture: new Fixture(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [FixtureDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FixtureDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixtureDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixture).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
