import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { TeamDetailPointDetailComponent } from 'app/entities/team-detail-point/team-detail-point-detail.component';
import { TeamDetailPoint } from 'app/shared/model/team-detail-point.model';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Detail Component', () => {
    let comp: TeamDetailPointDetailComponent;
    let fixture: ComponentFixture<TeamDetailPointDetailComponent>;
    const route = ({ data: of({ teamDetailPoint: new TeamDetailPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [TeamDetailPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TeamDetailPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamDetailPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teamDetailPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
