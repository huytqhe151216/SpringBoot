import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHuyGenre, HuyGenre } from 'app/shared/model/huy-genre.model';
import { HuyGenreService } from './huy-genre.service';

@Component({
  selector: 'jhi-huy-genre-update',
  templateUrl: './huy-genre-update.component.html'
})
export class HuyGenreUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected huyGenreService: HuyGenreService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyGenre }) => {
      this.updateForm(huyGenre);
    });
  }

  updateForm(huyGenre: IHuyGenre): void {
    this.editForm.patchValue({
      id: huyGenre.id,
      name: huyGenre.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const huyGenre = this.createFromForm();
    if (huyGenre.id !== undefined) {
      this.subscribeToSaveResponse(this.huyGenreService.update(huyGenre));
    } else {
      this.subscribeToSaveResponse(this.huyGenreService.create(huyGenre));
    }
  }

  private createFromForm(): IHuyGenre {
    return {
      ...new HuyGenre(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuyGenre>>): void {
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
}
