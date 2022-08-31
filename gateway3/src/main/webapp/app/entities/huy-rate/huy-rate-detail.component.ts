import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuyRate } from 'app/shared/model/huy-rate.model';

@Component({
  selector: 'jhi-huy-rate-detail',
  templateUrl: './huy-rate-detail.component.html'
})
export class HuyRateDetailComponent implements OnInit {
  huyRate: IHuyRate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huyRate }) => (this.huyRate = huyRate));
  }

  previousState(): void {
    window.history.back();
  }
}
