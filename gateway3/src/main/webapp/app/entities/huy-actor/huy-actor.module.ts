import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Gateway3SharedModule } from 'app/shared/shared.module';
import { HuyActorComponent } from './huy-actor.component';
import { HuyActorDetailComponent } from './huy-actor-detail.component';
import { HuyActorUpdateComponent } from './huy-actor-update.component';
import { HuyActorDeleteDialogComponent } from './huy-actor-delete-dialog.component';
import { huyActorRoute } from './huy-actor.route';

@NgModule({
  imports: [Gateway3SharedModule, RouterModule.forChild(huyActorRoute)],
  declarations: [HuyActorComponent, HuyActorDetailComponent, HuyActorUpdateComponent, HuyActorDeleteDialogComponent],
  entryComponents: [HuyActorDeleteDialogComponent]
})
export class Gateway3HuyActorModule {}
