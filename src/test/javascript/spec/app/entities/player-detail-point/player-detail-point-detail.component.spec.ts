import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { PlayerDetailPointDetailComponent } from 'app/entities/player-detail-point/player-detail-point-detail.component';
import { PlayerDetailPoint } from 'app/shared/model/player-detail-point.model';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Detail Component', () => {
    let comp: PlayerDetailPointDetailComponent;
    let fixture: ComponentFixture<PlayerDetailPointDetailComponent>;
    const route = ({ data: of({ playerDetailPoint: new PlayerDetailPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [PlayerDetailPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlayerDetailPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerDetailPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerDetailPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
