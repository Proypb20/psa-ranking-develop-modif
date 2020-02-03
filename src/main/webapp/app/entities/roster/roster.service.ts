import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
    return this.http.post<IRoster>(this.resourceUrl, roster, { observe: 'response' });
  }

  update(roster: IRoster): Observable<EntityResponseType> {
    return this.http.put<IRoster>(this.resourceUrl, roster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
