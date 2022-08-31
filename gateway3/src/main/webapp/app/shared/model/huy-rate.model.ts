import { Moment } from 'moment';
import { IHuyMovie } from 'app/shared/model/huy-movie.model';

export interface IHuyRate {
  id?: number;
  star?: number;
  content?: string;
  dateCreate?: Moment;
  movie?: IHuyMovie;
}

export class HuyRate implements IHuyRate {
  constructor(public id?: number, public star?: number, public content?: string, public dateCreate?: Moment, public movie?: IHuyMovie) {}
}
