import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class EventXmlService {
    public resourceUrl = SERVER_API_URL + 'api/events';
    constructor(protected http: HttpClient){};

 importXML() {
    return this.http.post<void>(this.resourceUrl + '/importXML', { observe: 'response' });
  }
}
