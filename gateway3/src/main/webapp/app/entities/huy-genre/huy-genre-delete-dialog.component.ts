import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHuyGenre } from 'app/shared/model/huy-genre.model';
import { HuyGenreService } from './huy-genre.service';

@Component({
  templateUrl: './huy-genre-delete-dialog.component.html'
})
export class HuyGenreDeleteDialogComponent {
  huyGenre?: IHuyGenre;

  constructor(protected huyGenreService: HuyGenreService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.huyGenreService.delete(id).subscribe(() => {
      this.eventManager.broadcast('huyGenreListModification');
      this.activeModal.close();
    });
  }
}
