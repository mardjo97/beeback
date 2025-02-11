import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HiveLocationResolve from './route/hive-location-routing-resolve.service';

const hiveLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/hive-location.component').then(m => m.HiveLocationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/hive-location-detail.component').then(m => m.HiveLocationDetailComponent),
    resolve: {
      hiveLocation: HiveLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/hive-location-update.component').then(m => m.HiveLocationUpdateComponent),
    resolve: {
      hiveLocation: HiveLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/hive-location-update.component').then(m => m.HiveLocationUpdateComponent),
    resolve: {
      hiveLocation: HiveLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hiveLocationRoute;
