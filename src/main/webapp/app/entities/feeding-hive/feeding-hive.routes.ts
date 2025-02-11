import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FeedingHiveResolve from './route/feeding-hive-routing-resolve.service';

const feedingHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/feeding-hive.component').then(m => m.FeedingHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/feeding-hive-detail.component').then(m => m.FeedingHiveDetailComponent),
    resolve: {
      feedingHive: FeedingHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/feeding-hive-update.component').then(m => m.FeedingHiveUpdateComponent),
    resolve: {
      feedingHive: FeedingHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/feeding-hive-update.component').then(m => m.FeedingHiveUpdateComponent),
    resolve: {
      feedingHive: FeedingHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedingHiveRoute;
