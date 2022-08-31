import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHuyRate } from 'app/shared/model/huy-rate.model';
import { HuyRateService } from './huy-rate.service';
import { HuyRateDeleteDialogComponent } from './huy-rate-delete-dialog.component';

@Component({
  selector: 'jhi-huy-rate',
  templateUrl: './huy-rate.component.html'
})
export class HuyRateComponent implements OnInit, OnDestroy {
  huyRates?: IHuyRate[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected huyRateService: HuyRateService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.huyRateService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHuyRate[]>) => (this.huyRates = res.body || []));
      return;
    }

    this.huyRateService.query().subscribe((res: HttpResponse<IHuyRate[]>) => (this.huyRates = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHuyRates();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHuyRate): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHuyRates(): void {
    this.eventSubscriber = this.eventManager.subscribe('huyRateListModification', () => this.loadAll());
  }

  delete(huyRate: IHuyRate): void {
    const modalRef = this.modalService.open(HuyRateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.huyRate = huyRate;
  }
}
