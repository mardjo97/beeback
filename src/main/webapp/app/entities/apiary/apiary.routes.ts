import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ApiaryResolve from './route/apiary-routing-resolve.service';

const apiaryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/apiary.component').then(m => m.ApiaryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/apiary-detail.component').then(m => m.ApiaryDetailComponent),
    resolve: {
      apiary: ApiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/apiary-update.component').then(m => m.ApiaryUpdateComponent),
    resolve: {
      apiary: ApiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/apiary-update.component').then(m => m.ApiaryUpdateComponent),
    resolve: {
      apiary: ApiaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apiaryRoute;
