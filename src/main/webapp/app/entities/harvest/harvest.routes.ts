import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HarvestResolve from './route/harvest-routing-resolve.service';

const harvestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/harvest.component').then(m => m.HarvestComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/harvest-detail.component').then(m => m.HarvestDetailComponent),
    resolve: {
      harvest: HarvestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/harvest-update.component').then(m => m.HarvestUpdateComponent),
    resolve: {
      harvest: HarvestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/harvest-update.component').then(m => m.HarvestUpdateComponent),
    resolve: {
      harvest: HarvestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default harvestRoute;
