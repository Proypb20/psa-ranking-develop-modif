import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { RosterDetailComponent } from 'app/entities/roster/roster-detail.component';
import { Roster } from 'app/shared/model/roster.model';

describe('Component Tests', () => {
  describe('Roster Management Detail Component', () => {
    let comp: RosterDetailComponent;
    let fixture: ComponentFixture<RosterDetailComponent>;
    const route = ({ data: of({ roster: new Roster(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [RosterDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RosterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RosterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.roster).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
