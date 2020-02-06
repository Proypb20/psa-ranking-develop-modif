import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsaRankingTestModule } from '../../../test.module';
import { PlayerPointDetailComponent } from 'app/entities/player-point/player-point-detail.component';
import { PlayerPoint } from 'app/shared/model/player-point.model';

describe('Component Tests', () => {
  describe('PlayerPoint Management Detail Component', () => {
    let comp: PlayerPointDetailComponent;
    let fixture: ComponentFixture<PlayerPointDetailComponent>;
    const route = ({ data: of({ playerPoint: new PlayerPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsaRankingTestModule],
        declarations: [PlayerPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlayerPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
