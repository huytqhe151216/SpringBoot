import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHuyActor } from 'app/shared/model/huy-actor.model';
import { HuyActorService } from './huy-actor.service';
import { HuyActorDeleteDialogComponent } from './huy-actor-delete-dialog.component';

@Component({
  selector: 'jhi-huy-actor',
  templateUrl: './huy-actor.component.html'
})
export class HuyActorComponent implements OnInit, OnDestroy {
  huyActors?: IHuyActor[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected huyActorService: HuyActorService,
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
      this.huyActorService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHuyActor[]>) => (this.huyActors = res.body || []));
      return;
    }

    this.huyActorService.query().subscribe((res: HttpResponse<IHuyActor[]>) => (this.huyActors = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHuyActors();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHuyActor): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHuyActors(): void {
    this.eventSubscriber = this.eventManager.subscribe('huyActorListModification', () => this.loadAll());
  }

  delete(huyActor: IHuyActor): void {
    const modalRef = this.modalService.open(HuyActorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.huyActor = huyActor;
  }
}
