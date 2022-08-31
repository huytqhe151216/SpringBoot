import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHuyMovie } from 'app/shared/model/huy-movie.model';

type EntityResponseType = HttpResponse<IHuyMovie>;
type EntityArrayResponseType = HttpResponse<IHuyMovie[]>;

@Injectable({ providedIn: 'root' })
export class HuyMovieService {
  public resourceUrl = SERVER_API_URL + 'api/huy-movies';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/huy-movies';

  constructor(protected http: HttpClient) {}

  create(huyMovie: IHuyMovie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyMovie);
    return this.http
      .post<IHuyMovie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(huyMovie: IHuyMovie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(huyMovie);
    return this.http
      .put<IHuyMovie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHuyMovie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyMovie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHuyMovie[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(huyMovie: IHuyMovie): IHuyMovie {
    const copy: IHuyMovie = Object.assign({}, huyMovie, {
      publishDate: huyMovie.publishDate && huyMovie.publishDate.isValid() ? huyMovie.publishDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.publishDate = res.body.publishDate ? moment(res.body.publishDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((huyMovie: IHuyMovie) => {
        huyMovie.publishDate = huyMovie.publishDate ? moment(huyMovie.publishDate) : undefined;
      });
    }
    return res;
  }
}
