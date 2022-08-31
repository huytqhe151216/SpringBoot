import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuyGenre } from 'app/shared/model/huy-genre.model';

@Component({
  selector: 'jhi-huy-genre-detail',
  templateUrl: './huy-genre-detail.component.html'
})
export class HuyGenreDetailComponent implements OnInit {
  huyGenre: IHuyGenre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyGenre }) => (this.huyGenre = huyGenre));
  }

  previousState(): void {
    window.history.back();
  }
}
