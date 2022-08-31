import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHuyMovie } from 'app/shared/model/huy-movie.model';
import { HuyMovieService } from './huy-movie.service';
import { HuyMovieDeleteDialogComponent } from './huy-movie-delete-dialog.component';

@Component({
  selector: 'jhi-huy-movie',
  templateUrl: './huy-movie.component.html'
})
export class HuyMovieComponent implements OnInit, OnDestroy {
  huyMovies?: IHuyMovie[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected huyMovieService: HuyMovieService,
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
      this.huyMovieService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHuyMovie[]>) => (this.huyMovies = res.body || []));
      return;
    }

    this.huyMovieService.query().subscribe((res: HttpResponse<IHuyMovie[]>) => (this.huyMovies = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHuyMovies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHuyMovie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHuyMovies(): void {
    this.eventSubscriber = this.eventManager.subscribe('huyMovieListModification', () => this.loadAll());
  }

  delete(huyMovie: IHuyMovie): void {
    const modalRef = this.modalService.open(HuyMovieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.huyMovie = huyMovie;
  }
}
