import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IHuyActor, HuyActor } from 'app/shared/model/huy-actor.model';
import { HuyActorService } from './huy-actor.service';

@Component({
  selector: 'jhi-huy-actor-update',
  templateUrl: './huy-actor-update.component.html'
})
export class HuyActorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    dob: [],
    nationality: []
  });

  constructor(protected huyActorService: HuyActorService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyActor }) => {
      if (!huyActor.id) {
        const today = moment().startOf('day');
        huyActor.dob = today;
      }

      this.updateForm(huyActor);
    });
  }

  updateForm(huyActor: IHuyActor): void {
    this.editForm.patchValue({
      id: huyActor.id,
      name: huyActor.name,
      dob: huyActor.dob ? huyActor.dob.format(DATE_TIME_FORMAT) : null,
      nationality: huyActor.nationality
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const huyActor = this.createFromForm();
    if (huyActor.id !== undefined) {
      this.subscribeToSaveResponse(this.huyActorService.update(huyActor));
    } else {
      this.subscribeToSaveResponse(this.huyActorService.create(huyActor));
    }
  }

  private createFromForm(): IHuyActor {
    return {
      ...new HuyActor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dob: this.editForm.get(['dob'])!.value ? moment(this.editForm.get(['dob'])!.value, DATE_TIME_FORMAT) : undefined,
      nationality: this.editForm.get(['nationality'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuyActor>>): void {
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
