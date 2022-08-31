import { IHuyMovie } from 'app/shared/model/huy-movie.model';

export interface IHuyGenre {
  id?: number;
  name?: string;
  movies?: IHuyMovie[];
}

export class HuyGenre implements IHuyGenre {
  constructor(public id?: number, public name?: string, public movies?: IHuyMovie[]) {}
}
