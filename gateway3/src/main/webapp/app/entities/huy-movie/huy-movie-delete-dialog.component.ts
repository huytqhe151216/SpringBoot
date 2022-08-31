import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHuyMovie } from 'app/shared/model/huy-movie.model';
import { HuyMovieService } from './huy-movie.service';

@Component({
  templateUrl: './huy-movie-delete-dialog.component.html'
})
export class HuyMovieDeleteDialogComponent {
  huyMovie?: IHuyMovie;

  constructor(protected huyMovieService: HuyMovieService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.huyMovieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('huyMovieListModification');
      this.activeModal.close();
    });
  }
}
