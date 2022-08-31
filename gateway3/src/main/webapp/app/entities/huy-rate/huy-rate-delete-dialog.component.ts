import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHuyRate } from 'app/shared/model/huy-rate.model';
import { HuyRateService } from './huy-rate.service';

@Component({
  templateUrl: './huy-rate-delete-dialog.component.html'
})
export class HuyRateDeleteDialogComponent {
  huyRate?: IHuyRate;

  constructor(protected huyRateService: HuyRateService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.huyRateService.delete(id).subscribe(() => {
      this.eventManager.broadcast('huyRateListModification');
      this.activeModal.close();
    });
  }
}
