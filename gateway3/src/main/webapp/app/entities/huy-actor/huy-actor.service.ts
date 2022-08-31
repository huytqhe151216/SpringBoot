import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHuyActor } from 'app/shared/model/huy-actor.model';

type EntityResponseType = HttpResponse<IHuyActor>;
type EntityArrayResponseType = HttpResponse<IHuyActor[]>;

@Injectable({ providedIn: 'root' })
export class HuyActorService {
  public resourceUrl = SERVER_API_URL + 'api/huy-actors';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/huy-actors';

  constructor(protected http: HttpClient) {}

  create(huyActor: IHuyActor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyActor);
    return this.http
      .post<IHuyActor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(huyActor: IHuyActor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyActor);
    return this.http
      .put<IHuyActor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHuyActor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyActor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyActor[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(huyActor: IHuyActor): IHuyActor {
    const copy: IHuyActor = Object.assign({}, huyActor, {
      dob: huyActor.dob && huyActor.dob.isValid() ? huyActor.dob.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dob = res.body.dob ? moment(res.body.dob) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((huyActor: IHuyActor) => {
        huyActor.dob = huyActor.dob ? moment(huyActor.dob) : undefined;
      });
    }
    return res;
  }
}
