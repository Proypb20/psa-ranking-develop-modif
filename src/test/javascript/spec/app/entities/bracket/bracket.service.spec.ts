import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { BracketService } from 'app/entities/bracket/bracket.service';
import { IBracket, Bracket } from 'app/shared/model/bracket.model';

describe('Service Tests', () => {
  describe('Bracket Service', () => {
    let injector: TestBed;
    let service: BracketService;
    let httpMock: HttpTestingController;
    let elemDefault: IBracket;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(BracketService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Bracket(0, 0, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should return a list of Bracket', () => {
        const returnedFromService = Object.assign(
          {
            teams: 1,
            teams5A: 1,
            teams5B: 1,
            teams6A: 1,
            teams6B: 1
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
