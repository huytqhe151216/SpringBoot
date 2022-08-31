import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Gateway3SharedModule } from 'app/shared/shared.module';
import { HuyGenreComponent } from './huy-genre.component';
import { HuyGenreDetailComponent } from './huy-genre-detail.component';
import { HuyGenreUpdateComponent } from './huy-genre-update.component';
import { HuyGenreDeleteDialogComponent } from './huy-genre-delete-dialog.component';
import { huyGenreRoute } from './huy-genre.route';

@NgModule({
  imports: [Gateway3SharedModule, RouterModule.forChild(huyGenreRoute)],
  declarations: [HuyGenreComponent, HuyGenreDetailComponent, HuyGenreUpdateComponent, HuyGenreDeleteDialogComponent],
  entryComponents: [HuyGenreDeleteDialogComponent]
})
export class Gateway3HuyGenreModule {}
