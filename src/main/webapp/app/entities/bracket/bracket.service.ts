import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBracket } from 'app/shared/model/bracket.model';

type EntityResponseType = HttpResponse<IBracket>;
type EntityArrayResponseType = HttpResponse<IBracket[]>;

@Injectable({ providedIn: 'root' })
export class BracketService {
  public resourceUrl = SERVER_API_URL + 'api/brackets';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBracket>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBracket[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
