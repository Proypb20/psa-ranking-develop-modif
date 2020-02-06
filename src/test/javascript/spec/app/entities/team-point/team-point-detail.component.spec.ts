import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { TeamPointDetailComponent } from 'app/entities/team-point/team-point-detail.component';
import { TeamPoint } from 'app/shared/model/team-point.model';

describe('Component Tests', () => {
  describe('TeamPoint Management Detail Component', () => {
    let comp: TeamPointDetailComponent;
    let fixture: ComponentFixture<TeamPointDetailComponent>;
    const route = ({ data: of({ teamPoint: new TeamPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [TeamPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TeamPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teamPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
