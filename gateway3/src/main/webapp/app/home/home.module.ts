import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Gateway3SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [Gateway3SharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class Gateway3HomeModule {}
