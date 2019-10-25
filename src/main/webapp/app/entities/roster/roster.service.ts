import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRoster } from 'app/shared/model/roster.model';

type EntityResponseType = HttpResponse<IRoster>;
type EntityArrayResponseType = HttpResponse<IRoster[]>;

@Injectable({ providedIn: 'root' })
export class RosterService {
  public resourceUrl = SERVER_API_URL + 'api/rosters';

  constructor(protected http: HttpClient) {}

  create(roster: IRoster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roster);
    return this.http
      .post<IRoster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(roster: IRoster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roster);
    return this.http
      .put<IRoster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRoster>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRoster[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(roster: IRoster): IRoster {
    const copy: IRoster = Object.assign({}, roster, {
      createDate: roster.createDate != null && roster.createDate.isValid() ? roster.createDate.toJSON() : null,
      updatedDate: roster.updatedDate != null && roster.updatedDate.isValid() ? roster.updatedDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.updatedDate = res.body.updatedDate != null ? moment(res.body.updatedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((roster: IRoster) => {
        roster.createDate = roster.createDate != null ? moment(roster.createDate) : null;
        roster.updatedDate = roster.updatedDate != null ? moment(roster.updatedDate) : null;
      });
    }
    return res;
  }
}
