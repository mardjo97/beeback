import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HiveResolve from './route/hive-routing-resolve.service';

const hiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/hive.component').then(m => m.HiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/hive-detail.component').then(m => m.HiveDetailComponent),
    resolve: {
      hive: HiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/hive-update.component').then(m => m.HiveUpdateComponent),
    resolve: {
      hive: HiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/hive-update.component').then(m => m.HiveUpdateComponent),
    resolve: {
      hive: HiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hiveRoute;
