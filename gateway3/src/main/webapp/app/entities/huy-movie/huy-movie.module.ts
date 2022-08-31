import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Gateway3SharedModule } from 'app/shared/shared.module';
import { HuyMovieComponent } from './huy-movie.component';
import { HuyMovieDetailComponent } from './huy-movie-detail.component';
import { HuyMovieUpdateComponent } from './huy-movie-update.component';
import { HuyMovieDeleteDialogComponent } from './huy-movie-delete-dialog.component';
import { huyMovieRoute } from './huy-movie.route';

@NgModule({
  imports: [Gateway3SharedModule, RouterModule.forChild(huyMovieRoute)],
  declarations: [HuyMovieComponent, HuyMovieDetailComponent, HuyMovieUpdateComponent, HuyMovieDeleteDialogComponent],
  entryComponents: [HuyMovieDeleteDialogComponent]
})
export class Gateway3HuyMovieModule {}
