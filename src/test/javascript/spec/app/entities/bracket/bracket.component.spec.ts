import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PbPointsTestModule } from '../../../test.module';
import { BracketComponent } from 'app/entities/bracket/bracket.component';
import { BracketService } from 'app/entities/bracket/bracket.service';
import { Bracket } from 'app/shared/model/bracket.model';

describe('Component Tests', () => {
  describe('Bracket Management Component', () => {
    let comp: BracketComponent;
    let fixture: ComponentFixture<BracketComponent>;
    let service: BracketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [BracketComponent],
        providers: []
      })
        .overrideTemplate(BracketComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BracketComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BracketService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Bracket(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.brackets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
