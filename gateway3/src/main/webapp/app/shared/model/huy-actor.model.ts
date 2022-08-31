import { Moment } from 'moment';
import { IHuyMovie } from 'app/shared/model/huy-movie.model';

export interface IHuyActor {
  id?: number;
  name?: string;
  dob?: Moment;
  nationality?: string;
  movies?: IHuyMovie[];
}

export class HuyActor implements IHuyActor {
  constructor(public id?: number, public name?: string, public dob?: Moment, public nationality?: string, public movies?: IHuyMovie[]) {}
}
