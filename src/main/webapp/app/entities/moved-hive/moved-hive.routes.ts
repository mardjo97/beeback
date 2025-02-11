import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MovedHiveResolve from './route/moved-hive-routing-resolve.service';

const movedHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/moved-hive.component').then(m => m.MovedHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/moved-hive-detail.component').then(m => m.MovedHiveDetailComponent),
    resolve: {
      movedHive: MovedHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/moved-hive-update.component').then(m => m.MovedHiveUpdateComponent),
    resolve: {
      movedHive: MovedHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/moved-hive-update.component').then(m => m.MovedHiveUpdateComponent),
    resolve: {
      movedHive: MovedHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default movedHiveRoute;
