import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHuyGenre } from 'app/shared/model/huy-genre.model';
import { HuyGenreService } from './huy-genre.service';
import { HuyGenreDeleteDialogComponent } from './huy-genre-delete-dialog.component';

@Component({
  selector: 'jhi-huy-genre',
  templateUrl: './huy-genre.component.html'
})
export class HuyGenreComponent implements OnInit, OnDestroy {
  huyGenres?: IHuyGenre[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected huyGenreService: HuyGenreService,
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
      this.huyGenreService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHuyGenre[]>) => (this.huyGenres = res.body || []));
      return;
    }

    this.huyGenreService.query().subscribe((res: HttpResponse<IHuyGenre[]>) => (this.huyGenres = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHuyGenres();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHuyGenre): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHuyGenres(): void {
    this.eventSubscriber = this.eventManager.subscribe('huyGenreListModification', () => this.loadAll());
  }

  delete(huyGenre: IHuyGenre): void {
    const modalRef = this.modalService.open(HuyGenreDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.huyGenre = huyGenre;
  }
}
