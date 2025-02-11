import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HarvestTypeResolve from './route/harvest-type-routing-resolve.service';

const harvestTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/harvest-type.component').then(m => m.HarvestTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/harvest-type-detail.component').then(m => m.HarvestTypeDetailComponent),
    resolve: {
      harvestType: HarvestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/harvest-type-update.component').then(m => m.HarvestTypeUpdateComponent),
    resolve: {
      harvestType: HarvestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/harvest-type-update.component').then(m => m.HarvestTypeUpdateComponent),
    resolve: {
      harvestType: HarvestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default harvestTypeRoute;
