import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHuyActor } from 'app/shared/model/huy-actor.model';
import { HuyActorService } from './huy-actor.service';

@Component({
  templateUrl: './huy-actor-delete-dialog.component.html'
})
export class HuyActorDeleteDialogComponent {
  huyActor?: IHuyActor;

  constructor(protected huyActorService: HuyActorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.huyActorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('huyActorListModification');
      this.activeModal.close();
    });
  }
}
