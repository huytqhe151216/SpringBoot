import { Moment } from 'moment';
import { IHuyRate } from 'app/shared/model/huy-rate.model';
import { IHuyGenre } from 'app/shared/model/huy-genre.model';
import { IHuyActor } from 'app/shared/model/huy-actor.model';

export interface IHuyMovie {
  id?: number;
  name?: string;
  director?: string;
  country?: string;
  writer?: string;
  duration?: number;
  publishDate?: Moment;
  contentSummary?: string;
  huyRates?: IHuyRate[];
  genres?: IHuyGenre[];
  actors?: IHuyActor[];
}

export class HuyMovie implements IHuyMovie {
  constructor(
    public id?: number,
    public name?: string,
    public director?: string,
    public country?: string,
    public writer?: string,
    public duration?: number,
    public publishDate?: Moment,
    public contentSummary?: string,
    public huyRates?: IHuyRate[],
    public genres?: IHuyGenre[],
    public actors?: IHuyActor[]
  ) {}
}
