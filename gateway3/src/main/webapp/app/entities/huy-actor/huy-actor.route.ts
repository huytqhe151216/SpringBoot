import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHuyActor, HuyActor } from 'app/shared/model/huy-actor.model';
import { HuyActorService } from './huy-actor.service';
import { HuyActorComponent } from './huy-actor.component';
import { HuyActorDetailComponent } from './huy-actor-detail.component';
import { HuyActorUpdateComponent } from './huy-actor-update.component';

@Injectable({ providedIn: 'root' })
export class HuyActorResolve implements Resolve<IHuyActor> {
  constructor(private service: HuyActorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuyActor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((huyActor: HttpResponse<HuyActor>) => {
          if (huyActor.body) {
            return of(huyActor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HuyActor());
  }
}

export const huyActorRoute: Routes = [
  {
    path: '',
    component: HuyActorComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyActors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HuyActorDetailComponent,
    resolve: {
      huyActor: HuyActorResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyActors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HuyActorUpdateComponent,
    resolve: {
      huyActor: HuyActorResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyActors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HuyActorUpdateComponent,
    resolve: {
      huyActor: HuyActorResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'HuyActors'
    },
    canActivate: [UserRouteAccessService]
  }
];
