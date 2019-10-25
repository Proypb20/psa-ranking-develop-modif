import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITeam } from 'app/shared/model/team.model';

type EntityResponseType = HttpResponse<ITeam>;
type EntityArrayResponseType = HttpResponse<ITeam[]>;

@Injectable({ providedIn: 'root' })
export class TeamService {
  public resourceUrl = SERVER_API_URL + 'api/teams';

  constructor(protected http: HttpClient) {}

  create(team: ITeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(team);
    return this.http
      .post<ITeam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(team: ITeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(team);
    return this.http
      .put<ITeam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITeam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITeam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(team: ITeam): ITeam {
    const copy: ITeam = Object.assign({}, team, {
      createDate: team.createDate != null && team.createDate.isValid() ? team.createDate.toJSON() : null,
      updatedDate: team.updatedDate != null && team.updatedDate.isValid() ? team.updatedDate.toJSON() : null
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
      res.body.forEach((team: ITeam) => {
        team.createDate = team.createDate != null ? moment(team.createDate) : null;
        team.updatedDate = team.updatedDate != null ? moment(team.updatedDate) : null;
      });
    }
    return res;
  }
}
