import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEvent } from 'app/shared/model/event.model';

type EntityResponseType = HttpResponse<IEvent>;
type EntityArrayResponseType = HttpResponse<IEvent[]>;

@Injectable({ providedIn: 'root' })
export class EventService {
  public resourceUrl = SERVER_API_URL + 'api/events';

  constructor(protected http: HttpClient) {}

  create(event: IEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .post<IEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(event: IEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .put<IEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(event: IEvent): IEvent {
    const copy: IEvent = Object.assign({}, event, {
      fromDate: event.fromDate != null && event.fromDate.isValid() ? event.fromDate.format(DATE_FORMAT) : null,
      endDate: event.endDate != null && event.endDate.isValid() ? event.endDate.format(DATE_FORMAT) : null,
      endInscriptionDate:
        event.endInscriptionDate != null && event.endInscriptionDate.isValid() ? event.endInscriptionDate.format(DATE_FORMAT) : null,
      createDate: event.createDate != null && event.createDate.isValid() ? event.createDate.toJSON() : null,
      updatedDate: event.updatedDate != null && event.updatedDate.isValid() ? event.updatedDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fromDate = res.body.fromDate != null ? moment(res.body.fromDate) : null;
      res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
      res.body.endInscriptionDate = res.body.endInscriptionDate != null ? moment(res.body.endInscriptionDate) : null;
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.updatedDate = res.body.updatedDate != null ? moment(res.body.updatedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((event: IEvent) => {
        event.fromDate = event.fromDate != null ? moment(event.fromDate) : null;
        event.endDate = event.endDate != null ? moment(event.endDate) : null;
        event.endInscriptionDate = event.endInscriptionDate != null ? moment(event.endInscriptionDate) : null;
        event.createDate = event.createDate != null ? moment(event.createDate) : null;
        event.updatedDate = event.updatedDate != null ? moment(event.updatedDate) : null;
      });
    }
    return res;
  }
}
