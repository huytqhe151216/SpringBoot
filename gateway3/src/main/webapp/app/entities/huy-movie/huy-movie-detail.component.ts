import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuyMovie } from 'app/shared/model/huy-movie.model';

@Component({
  selector: 'jhi-huy-movie-detail',
  templateUrl: './huy-movie-detail.component.html'
})
export class HuyMovieDetailComponent implements OnInit {
  huyMovie: IHuyMovie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyMovie }) => (this.huyMovie = huyMovie));
  }

  previousState(): void {
    window.history.back();
  }
}
