import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITeamPoint } from 'app/shared/model/team-point.model';

type EntityResponseType = HttpResponse<ITeamPoint>;
type EntityArrayResponseType = HttpResponse<ITeamPoint[]>;

@Injectable({ providedIn: 'root' })
export class TeamPointService {
  public resourceUrl = SERVER_API_URL + 'api/team-points';

  constructor(protected http: HttpClient) {}

  create(teamPoint: ITeamPoint): Observable<EntityResponseType> {
    return this.http.post<ITeamPoint>(this.resourceUrl, teamPoint, { observe: 'response' });
  }

  update(teamPoint: ITeamPoint): Observable<EntityResponseType> {
    return this.http.put<ITeamPoint>(this.resourceUrl, teamPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
