import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITournament } from 'app/shared/model/tournament.model';

type EntityResponseType = HttpResponse<ITournament>;
type EntityArrayResponseType = HttpResponse<ITournament[]>;

@Injectable({ providedIn: 'root' })
export class TournamentService {
  public resourceUrl = SERVER_API_URL + 'api/tournaments';

  constructor(protected http: HttpClient) {}

  create(tournament: ITournament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tournament);
    return this.http
      .post<ITournament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tournament: ITournament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tournament);
    return this.http
      .put<ITournament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITournament>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITournament[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(tournament: ITournament): ITournament {
    const copy: ITournament = Object.assign({}, tournament, {
      createDate: tournament.createDate != null && tournament.createDate.isValid() ? tournament.createDate.toJSON() : null,
      updatedDate: tournament.updatedDate != null && tournament.updatedDate.isValid() ? tournament.updatedDate.toJSON() : null
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
      res.body.forEach((tournament: ITournament) => {
        tournament.createDate = tournament.createDate != null ? moment(tournament.createDate) : null;
        tournament.updatedDate = tournament.updatedDate != null ? moment(tournament.updatedDate) : null;
      });
    }
    return res;
  }
}
