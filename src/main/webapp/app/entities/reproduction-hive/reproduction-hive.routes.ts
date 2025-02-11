import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReproductionHiveResolve from './route/reproduction-hive-routing-resolve.service';

const reproductionHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reproduction-hive.component').then(m => m.ReproductionHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reproduction-hive-detail.component').then(m => m.ReproductionHiveDetailComponent),
    resolve: {
      reproductionHive: ReproductionHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reproduction-hive-update.component').then(m => m.ReproductionHiveUpdateComponent),
    resolve: {
      reproductionHive: ReproductionHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reproduction-hive-update.component').then(m => m.ReproductionHiveUpdateComponent),
    resolve: {
      reproductionHive: ReproductionHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reproductionHiveRoute;
