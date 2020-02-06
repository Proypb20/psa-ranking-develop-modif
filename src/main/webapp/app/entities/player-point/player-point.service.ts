import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlayerPoint } from 'app/shared/model/player-point.model';

type EntityResponseType = HttpResponse<IPlayerPoint>;
type EntityArrayResponseType = HttpResponse<IPlayerPoint[]>;

@Injectable({ providedIn: 'root' })
export class PlayerPointService {
  public resourceUrl = SERVER_API_URL + 'api/player-points';

  constructor(protected http: HttpClient) {}

  create(playerPoint: IPlayerPoint): Observable<EntityResponseType> {
    return this.http.post<IPlayerPoint>(this.resourceUrl, playerPoint, { observe: 'response' });
  }

  update(playerPoint: IPlayerPoint): Observable<EntityResponseType> {
    return this.http.put<IPlayerPoint>(this.resourceUrl, playerPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
