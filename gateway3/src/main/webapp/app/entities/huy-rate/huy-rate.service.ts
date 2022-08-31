import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHuyRate } from 'app/shared/model/huy-rate.model';

type EntityResponseType = HttpResponse<IHuyRate>;
type EntityArrayResponseType = HttpResponse<IHuyRate[]>;

@Injectable({ providedIn: 'root' })
export class HuyRateService {
  public resourceUrl = SERVER_API_URL + 'api/huy-rates';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/huy-rates';

  constructor(protected http: HttpClient) {}

  create(huyRate: IHuyRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyRate);
    return this.http
      .post<IHuyRate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(huyRate: IHuyRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyRate);
    return this.http
      .put<IHuyRate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHuyRate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyRate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyRate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(huyRate: IHuyRate): IHuyRate {
    const copy: IHuyRate = Object.assign({}, huyRate, {
      dateCreate: huyRate.dateCreate && huyRate.dateCreate.isValid() ? huyRate.dateCreate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreate = res.body.dateCreate ? moment(res.body.dateCreate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((huyRate: IHuyRate) => {
        huyRate.dateCreate = huyRate.dateCreate ? moment(huyRate.dateCreate) : undefined;
      });
    }
    return res;
  }
}
