import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import GoodHarvestHiveResolve from './route/good-harvest-hive-routing-resolve.service';

const goodHarvestHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/good-harvest-hive.component').then(m => m.GoodHarvestHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/good-harvest-hive-detail.component').then(m => m.GoodHarvestHiveDetailComponent),
    resolve: {
      goodHarvestHive: GoodHarvestHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/good-harvest-hive-update.component').then(m => m.GoodHarvestHiveUpdateComponent),
    resolve: {
      goodHarvestHive: GoodHarvestHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/good-harvest-hive-update.component').then(m => m.GoodHarvestHiveUpdateComponent),
    resolve: {
      goodHarvestHive: GoodHarvestHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default goodHarvestHiveRoute;
