import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PersonService } from 'app/entities/person/person.service';
import { IPerson, Person } from 'app/shared/model/person.model';

describe('Service Tests', () => {
  describe('Person Service', () => {
    let injector: TestBed;
    let service: PersonService;
    let httpMock: HttpTestingController;
    let elemDefault: IPerson;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PersonService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Person(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate,
        false,
        currentDate,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            bornDate: currentDate.format(DATE_TIME_FORMAT),
            eraseDate: currentDate.format(DATE_TIME_FORMAT),
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Person', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            bornDate: currentDate.format(DATE_TIME_FORMAT),
            eraseDate: currentDate.format(DATE_TIME_FORMAT),
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            bornDate: currentDate,
            eraseDate: currentDate,
            createDate: currentDate,
            updatedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Person(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Person', () => {
        const returnedFromService = Object.assign(
          {
            names: 'BBBBBB',
            surnames: 'BBBBBB',
            email: 'BBBBBB',
            phone: 'BBBBBB',
            bornDate: currentDate.format(DATE_TIME_FORMAT),
            psaId: 'BBBBBB',
            eraseDate: currentDate.format(DATE_TIME_FORMAT),
            active: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            bornDate: currentDate,
            eraseDate: currentDate,
            createDate: currentDate,
            updatedDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Person', () => {
        const returnedFromService = Object.assign(
          {
            names: 'BBBBBB',
            surnames: 'BBBBBB',
            email: 'BBBBBB',
            phone: 'BBBBBB',
            bornDate: currentDate.format(DATE_TIME_FORMAT),
            psaId: 'BBBBBB',
            eraseDate: currentDate.format(DATE_TIME_FORMAT),
            active: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            bornDate: currentDate,
            eraseDate: currentDate,
            createDate: currentDate,
            updatedDate: currentDate
          },
          returnedFromService
        );
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

      it('should delete a Person', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
