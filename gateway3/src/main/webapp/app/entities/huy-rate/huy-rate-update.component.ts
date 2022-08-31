import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IHuyRate, HuyRate } from 'app/shared/model/huy-rate.model';
import { HuyRateService } from './huy-rate.service';
import { IHuyMovie } from 'app/shared/model/huy-movie.model';
import { HuyMovieService } from 'app/entities/huy-movie/huy-movie.service';

@Component({
  selector: 'jhi-huy-rate-update',
  templateUrl: './huy-rate-update.component.html'
})
export class HuyRateUpdateComponent implements OnInit {
  isSaving = false;
  huymovies: IHuyMovie[] = [];

  editForm = this.fb.group({
    id: [],
    star: [null, [Validators.required, Validators.min(1), Validators.max(5)]],
    content: [],
    dateCreate: [],
    movie: []
  });

  constructor(
    protected huyRateService: HuyRateService,
    protected huyMovieService: HuyMovieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyRate }) => {
      if (!huyRate.id) {
        const today = moment().startOf('day');
        huyRate.dateCreate = today;
      }

      this.updateForm(huyRate);

      this.huyMovieService.query().subscribe((res: HttpResponse<IHuyMovie[]>) => (this.huymovies = res.body || []));
    });
  }

  updateForm(huyRate: IHuyRate): void {
    this.editForm.patchValue({
      id: huyRate.id,
      star: huyRate.star,
      content: huyRate.content,
      dateCreate: huyRate.dateCreate ? huyRate.dateCreate.format(DATE_TIME_FORMAT) : null,
      movie: huyRate.movie
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const huyRate = this.createFromForm();
    if (huyRate.id !== undefined) {
      this.subscribeToSaveResponse(this.huyRateService.update(huyRate));
    } else {
      this.subscribeToSaveResponse(this.huyRateService.create(huyRate));
    }
  }

  private createFromForm(): IHuyRate {
    return {
      ...new HuyRate(),
      id: this.editForm.get(['id'])!.value,
      star: this.editForm.get(['star'])!.value,
      content: this.editForm.get(['content'])!.value,
      dateCreate: this.editForm.get(['dateCreate'])!.value ? moment(this.editForm.get(['dateCreate'])!.value, DATE_TIME_FORMAT) : undefined,
      movie: this.editForm.get(['movie'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuyRate>>): void {
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

  trackById(index: number, item: IHuyMovie): any {
    return item.id;
  }
}
