import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'huy-movie',
        loadChildren: () => import('./huy-movie/huy-movie.module').then(m => m.Gateway3HuyMovieModule)
      },
      {
        path: 'huy-genre',
        loadChildren: () => import('./huy-genre/huy-genre.module').then(m => m.Gateway3HuyGenreModule)
      },
      {
        path: 'huy-actor',
        loadChildren: () => import('./huy-actor/huy-actor.module').then(m => m.Gateway3HuyActorModule)
      },
      {
        path: 'huy-rate',
        loadChildren: () => import('./huy-rate/huy-rate.module').then(m => m.Gateway3HuyRateModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class Gateway3EntityModule {}
