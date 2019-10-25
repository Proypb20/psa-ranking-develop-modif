import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDocType } from 'app/shared/model/doc-type.model';

type EntityResponseType = HttpResponse<IDocType>;
type EntityArrayResponseType = HttpResponse<IDocType[]>;

@Injectable({ providedIn: 'root' })
export class DocTypeService {
  public resourceUrl = SERVER_API_URL + 'api/doc-types';

  constructor(protected http: HttpClient) {}

  create(docType: IDocType): Observable<EntityResponseType> {
    return this.http.post<IDocType>(this.resourceUrl, docType, { observe: 'response' });
  }

  update(docType: IDocType): Observable<EntityResponseType> {
    return this.http.put<IDocType>(this.resourceUrl, docType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
