import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHuyGenre } from 'app/shared/model/huy-genre.model';

type EntityResponseType = HttpResponse<IHuyGenre>;
type EntityArrayResponseType = HttpResponse<IHuyGenre[]>;

@Injectable({ providedIn: 'root' })
export class HuyGenreService {
  public resourceUrl = SERVER_API_URL + 'api/huy-genres';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/huy-genres';

  constructor(protected http: HttpClient) {}

  create(huyGenre: IHuyGenre): Observable<EntityResponseType> {
    return this.http.post<IHuyGenre>(this.resourceUrl, huyGenre, { observe: 'response' });
  }

  update(huyGenre: IHuyGenre): Observable<EntityResponseType> {
    return this.http.put<IHuyGenre>(this.resourceUrl, huyGenre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHuyGenre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHuyGenre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHuyGenre[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
