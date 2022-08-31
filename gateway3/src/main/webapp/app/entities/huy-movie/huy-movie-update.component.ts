import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IHuyMovie, HuyMovie } from 'app/shared/model/huy-movie.model';
import { HuyMovieService } from './huy-movie.service';
import { IHuyGenre } from 'app/shared/model/huy-genre.model';
import { HuyGenreService } from 'app/entities/huy-genre/huy-genre.service';
import { IHuyActor } from 'app/shared/model/huy-actor.model';
import { HuyActorService } from 'app/entities/huy-actor/huy-actor.service';

type SelectableEntity = IHuyGenre | IHuyActor;

@Component({
  selector: 'jhi-huy-movie-update',
  templateUrl: './huy-movie-update.component.html'
})
export class HuyMovieUpdateComponent implements OnInit {
  isSaving = false;
  huygenres: IHuyGenre[] = [];
  huyactors: IHuyActor[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    director: [],
    country: [],
    writer: [],
    duration: [],
    publishDate: [],
    contentSummary: [],
    genres: [],
    actors: []
  });

  constructor(
    protected huyMovieService: HuyMovieService,
    protected huyGenreService: HuyGenreService,
    protected huyActorService: HuyActorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyMovie }) => {
      if (!huyMovie.id) {
        const today = moment().startOf('day');
        huyMovie.publishDate = today;
      }

      this.updateForm(huyMovie);

      this.huyGenreService.query().subscribe((res: HttpResponse<IHuyGenre[]>) => (this.huygenres = res.body || []));

      this.huyActorService.query().subscribe((res: HttpResponse<IHuyActor[]>) => (this.huyactors = res.body || []));
    });
  }

  updateForm(huyMovie: IHuyMovie): void {
    this.editForm.patchValue({
      id: huyMovie.id,
      name: huyMovie.name,
      director: huyMovie.director,
      country: huyMovie.country,
      writer: huyMovie.writer,
      duration: huyMovie.duration,
      publishDate: huyMovie.publishDate ? huyMovie.publishDate.format(DATE_TIME_FORMAT) : null,
      contentSummary: huyMovie.contentSummary,
      genres: huyMovie.genres,
      actors: huyMovie.actors
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const huyMovie = this.createFromForm();
    if (huyMovie.id !== undefined) {
      this.subscribeToSaveResponse(this.huyMovieService.update(huyMovie));
    } else {
      this.subscribeToSaveResponse(this.huyMovieService.create(huyMovie));
    }
  }

  private createFromForm(): IHuyMovie {
    return {
      ...new HuyMovie(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      director: this.editForm.get(['director'])!.value,
      country: this.editForm.get(['country'])!.value,
      writer: this.editForm.get(['writer'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      publishDate: this.editForm.get(['publishDate'])!.value
        ? moment(this.editForm.get(['publishDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contentSummary: this.editForm.get(['contentSummary'])!.value,
      genres: this.editForm.get(['genres'])!.value,
      actors: this.editForm.get(['actors'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuyMovie>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableEntity[], option: SelectableEntity): SelectableEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
