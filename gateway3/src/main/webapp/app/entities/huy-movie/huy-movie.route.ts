import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHuyMovie, HuyMovie } from 'app/shared/model/huy-movie.model';
import { HuyMovieService } from './huy-movie.service';
import { HuyMovieComponent } from './huy-movie.component';
import { HuyMovieDetailComponent } from './huy-movie-detail.component';
import { HuyMovieUpdateComponent } from './huy-movie-update.component';

@Injectable({ providedIn: 'root' })
export class HuyMovieResolve implements Resolve<IHuyMovie> {
  constructor(private service: HuyMovieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuyMovie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((huyMovie: HttpResponse<HuyMovie>) => {
          if (huyMovie.body) {
            return of(huyMovie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HuyMovie());
  }
}

export const huyMovieRoute: Routes = [
  {
    path: '',
    component: HuyMovieComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyMovies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HuyMovieDetailComponent,
    resolve: {
      huyMovie: HuyMovieResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyMovies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HuyMovieUpdateComponent,
    resolve: {
      huyMovie: HuyMovieResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyMovies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HuyMovieUpdateComponent,
    resolve: {
      huyMovie: HuyMovieResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyMovies'
    },
    canActivate: [UserRouteAccessService]
  }
];
