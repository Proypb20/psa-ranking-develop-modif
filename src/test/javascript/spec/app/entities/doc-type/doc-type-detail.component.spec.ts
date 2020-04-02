import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PbPointsTestModule } from '../../../test.module';
import { DocTypeDetailComponent } from 'app/entities/doc-type/doc-type-detail.component';
import { DocType } from 'app/shared/model/doc-type.model';

describe('Component Tests', () => {
  describe('DocType Management Detail Component', () => {
    let comp: DocTypeDetailComponent;
    let fixture: ComponentFixture<DocTypeDetailComponent>;
    const route = ({ data: of({ docType: new DocType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PbPointsTestModule],
        declarations: [DocTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DocTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.docType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
