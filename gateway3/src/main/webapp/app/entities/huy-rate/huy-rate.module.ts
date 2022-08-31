import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Gateway3SharedModule } from 'app/shared/shared.module';
import { HuyRateComponent } from './huy-rate.component';
import { HuyRateDetailComponent } from './huy-rate-detail.component';
import { HuyRateUpdateComponent } from './huy-rate-update.component';
import { HuyRateDeleteDialogComponent } from './huy-rate-delete-dialog.component';
import { huyRateRoute } from './huy-rate.route';

@NgModule({
  imports: [Gateway3SharedModule, RouterModule.forChild(huyRateRoute)],
  declarations: [HuyRateComponent, HuyRateDetailComponent, HuyRateUpdateComponent, HuyRateDeleteDialogComponent],
  entryComponents: [HuyRateDeleteDialogComponent]
})
export class Gateway3HuyRateModule {}
