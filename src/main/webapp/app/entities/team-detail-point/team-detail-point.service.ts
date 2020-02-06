import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITeamDetailPoint } from 'app/shared/model/team-detail-point.model';

type EntityResponseType = HttpResponse<ITeamDetailPoint>;
type EntityArrayResponseType = HttpResponse<ITeamDetailPoint[]>;

@Injectable({ providedIn: 'root' })
export class TeamDetailPointService {
  public resourceUrl = SERVER_API_URL + 'api/team-detail-points';

  constructor(protected http: HttpClient) {}

  create(teamDetailPoint: ITeamDetailPoint): Observable<EntityResponseType> {
    return this.http.post<ITeamDetailPoint>(this.resourceUrl, teamDetailPoint, { observe: 'response' });
  }

  update(teamDetailPoint: ITeamDetailPoint): Observable<EntityResponseType> {
    return this.http.put<ITeamDetailPoint>(this.resourceUrl, teamDetailPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamDetailPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamDetailPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
