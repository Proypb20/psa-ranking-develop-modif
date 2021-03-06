import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';
import { IUserExtra, UserExtra } from 'app/shared/model/user-extra.model';

describe('Service Tests', () => {
  describe('UserExtra Service', () => {
    let injector: TestBed;
    let service: UserExtraService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserExtra;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(UserExtraService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new UserExtra(0, 'AAAAAAA', 'AAAAAAA', currentDate, 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            bornDate: currentDate.format(DATE_FORMAT)
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

      it('should create a UserExtra', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            bornDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            bornDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new UserExtra(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a UserExtra', () => {
        const returnedFromService = Object.assign(
          {
            numDoc: 'BBBBBB',
            phone: 'BBBBBB',
            bornDate: currentDate.format(DATE_FORMAT),
            picture: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            bornDate: currentDate
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

      it('should return a list of UserExtra', () => {
        const returnedFromService = Object.assign(
          {
            numDoc: 'BBBBBB',
            phone: 'BBBBBB',
            bornDate: currentDate.format(DATE_FORMAT),
            picture: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            bornDate: currentDate
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

      it('should delete a UserExtra', () => {
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
