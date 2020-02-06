import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlayerDetailPoint } from 'app/shared/model/player-detail-point.model';

type EntityResponseType = HttpResponse<IPlayerDetailPoint>;
type EntityArrayResponseType = HttpResponse<IPlayerDetailPoint[]>;

@Injectable({ providedIn: 'root' })
export class PlayerDetailPointService {
  public resourceUrl = SERVER_API_URL + 'api/player-detail-points';

  constructor(protected http: HttpClient) {}

  create(playerDetailPoint: IPlayerDetailPoint): Observable<EntityResponseType> {
    return this.http.post<IPlayerDetailPoint>(this.resourceUrl, playerDetailPoint, { observe: 'response' });
  }

  update(playerDetailPoint: IPlayerDetailPoint): Observable<EntityResponseType> {
    return this.http.put<IPlayerDetailPoint>(this.resourceUrl, playerDetailPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerDetailPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerDetailPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
