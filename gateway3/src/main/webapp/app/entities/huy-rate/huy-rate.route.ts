import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHuyRate, HuyRate } from 'app/shared/model/huy-rate.model';
import { HuyRateService } from './huy-rate.service';
import { HuyRateComponent } from './huy-rate.component';
import { HuyRateDetailComponent } from './huy-rate-detail.component';
import { HuyRateUpdateComponent } from './huy-rate-update.component';

@Injectable({ providedIn: 'root' })
export class HuyRateResolve implements Resolve<IHuyRate> {
  constructor(private service: HuyRateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuyRate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((huyRate: HttpResponse<HuyRate>) => {
          if (huyRate.body) {
            return of(huyRate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HuyRate());
  }
}

export const huyRateRoute: Routes = [
  {
    path: '',
    component: HuyRateComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyRates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HuyRateDetailComponent,
    resolve: {
      huyRate: HuyRateResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyRates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HuyRateUpdateComponent,
    resolve: {
      huyRate: HuyRateResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyRates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HuyRateUpdateComponent,
    resolve: {
      huyRate: HuyRateResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyRates'
    },
    canActivate: [UserRouteAccessService]
  }
];
