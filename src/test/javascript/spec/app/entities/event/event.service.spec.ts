import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EventService } from 'app/entities/event/event.service';
import { IEvent, Event } from 'app/shared/model/event.model';
import { Status } from 'app/shared/model/enumerations/status.model';

describe('Service Tests', () => {
  describe('Event Service', () => {
    let injector: TestBed;
    let service: EventService;
    let httpMock: HttpTestingController;
    let elemDefault: IEvent;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(EventService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Event(0, 'AAAAAAA', currentDate, currentDate, currentDate, Status.CREATED, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fromDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            endInscriptionDate: currentDate.format(DATE_FORMAT),
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

      it('should create a Event', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fromDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            endInscriptionDate: currentDate.format(DATE_FORMAT),
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fromDate: currentDate,
            endDate: currentDate,
            endInscriptionDate: currentDate,
            createDate: currentDate,
            updatedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Event(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Event', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            fromDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            endInscriptionDate: currentDate.format(DATE_FORMAT),
            status: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fromDate: currentDate,
            endDate: currentDate,
            endInscriptionDate: currentDate,
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

      it('should return a list of Event', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            fromDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            endInscriptionDate: currentDate.format(DATE_FORMAT),
            status: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            updatedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fromDate: currentDate,
            endDate: currentDate,
            endInscriptionDate: currentDate,
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

      it('should delete a Event', () => {
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
