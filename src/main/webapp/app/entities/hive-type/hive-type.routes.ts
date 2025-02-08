import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HiveTypeResolve from './route/hive-type-routing-resolve.service';

const hiveTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/hive-type.component').then(m => m.HiveTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/hive-type-detail.component').then(m => m.HiveTypeDetailComponent),
    resolve: {
      hiveType: HiveTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/hive-type-update.component').then(m => m.HiveTypeUpdateComponent),
    resolve: {
      hiveType: HiveTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/hive-type-update.component').then(m => m.HiveTypeUpdateComponent),
    resolve: {
      hiveType: HiveTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hiveTypeRoute;
