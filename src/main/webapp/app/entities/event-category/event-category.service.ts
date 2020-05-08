import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEventCategory } from 'app/shared/model/event-category.model';

type EntityResponseType = HttpResponse<IEventCategory>;
type EntityArrayResponseType = HttpResponse<IEventCategory[]>;

@Injectable({ providedIn: 'root' })
export class EventCategoryService {
  public resourceUrl = SERVER_API_URL + 'api/event-categories';

  constructor(protected http: HttpClient) {}

  create(eventCategory: IEventCategory): Observable<EntityResponseType> {
    return this.http.post<IEventCategory>(this.resourceUrl, eventCategory, { observe: 'response' });
  }

  update(eventCategory: IEventCategory): Observable<EntityResponseType> {
    return this.http.put<IEventCategory>(this.resourceUrl, eventCategory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  enableUpdate(id: number): Observable<HttpResponse<number>> {
     const options = createRequestOption(id);
     return this.http.get<number>(`${this.resourceUrl}/upd/${id}`, { params: options, observe: 'response' });
  }
}
