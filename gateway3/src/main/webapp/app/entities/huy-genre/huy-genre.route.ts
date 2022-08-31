import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHuyGenre, HuyGenre } from 'app/shared/model/huy-genre.model';
import { HuyGenreService } from './huy-genre.service';
import { HuyGenreComponent } from './huy-genre.component';
import { HuyGenreDetailComponent } from './huy-genre-detail.component';
import { HuyGenreUpdateComponent } from './huy-genre-update.component';

@Injectable({ providedIn: 'root' })
export class HuyGenreResolve implements Resolve<IHuyGenre> {
  constructor(private service: HuyGenreService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuyGenre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((huyGenre: HttpResponse<HuyGenre>) => {
          if (huyGenre.body) {
            return of(huyGenre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HuyGenre());
  }
}

export const huyGenreRoute: Routes = [
  {
    path: '',
    component: HuyGenreComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyGenres'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HuyGenreDetailComponent,
    resolve: {
      huyGenre: HuyGenreResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyGenres'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HuyGenreUpdateComponent,
    resolve: {
      huyGenre: HuyGenreResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyGenres'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HuyGenreUpdateComponent,
    resolve: {
      huyGenre: HuyGenreResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyGenres'
    },
    canActivate: [UserRouteAccessService]
  }
];
