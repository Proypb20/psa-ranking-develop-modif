import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { EventCategoryDetailComponent } from 'app/entities/event-category/event-category-detail.component';
import { EventCategory } from 'app/shared/model/event-category.model';

describe('Component Tests', () => {
  describe('EventCategory Management Detail Component', () => {
    let comp: EventCategoryDetailComponent;
    let fixture: ComponentFixture<EventCategoryDetailComponent>;
    const route = ({ data: of({ eventCategory: new EventCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [EventCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EventCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.eventCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
