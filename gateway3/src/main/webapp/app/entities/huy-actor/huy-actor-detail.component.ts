import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuyActor } from 'app/shared/model/huy-actor.model';

@Component({
  selector: 'jhi-huy-actor-detail',
  templateUrl: './huy-actor-detail.component.html'
})
export class HuyActorDetailComponent implements OnInit {
  huyActor: IHuyActor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyActor }) => (this.huyActor = huyActor));
  }

  previousState(): void {
    window.history.back();
  }
}
