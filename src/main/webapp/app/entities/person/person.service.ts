import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPerson } from 'app/shared/model/person.model';

type EntityResponseType = HttpResponse<IPerson>;
type EntityArrayResponseType = HttpResponse<IPerson[]>;

@Injectable({ providedIn: 'root' })
export class PersonService {
  public resourceUrl = SERVER_API_URL + 'api/people';

  constructor(protected http: HttpClient) {}

  create(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .post<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .put<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(person: IPerson): IPerson {
    const copy: IPerson = Object.assign({}, person, {
      eraseDate: person.eraseDate != null && person.eraseDate.isValid() ? person.eraseDate.toJSON() : null,
      createDate: person.createDate != null && person.createDate.isValid() ? person.createDate.toJSON() : null,
      updatedDate: person.updatedDate != null && person.updatedDate.isValid() ? person.updatedDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.eraseDate = res.body.eraseDate != null ? moment(res.body.eraseDate) : null;
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.updatedDate = res.body.updatedDate != null ? moment(res.body.updatedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((person: IPerson) => {
        person.eraseDate = person.eraseDate != null ? moment(person.eraseDate) : null;
        person.createDate = person.createDate != null ? moment(person.createDate) : null;
        person.updatedDate = person.updatedDate != null ? moment(person.updatedDate) : null;
      });
    }
    return res;
  }
}
