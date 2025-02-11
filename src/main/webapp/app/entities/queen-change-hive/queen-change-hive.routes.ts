import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import QueenChangeHiveResolve from './route/queen-change-hive-routing-resolve.service';

const queenChangeHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/queen-change-hive.component').then(m => m.QueenChangeHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/queen-change-hive-detail.component').then(m => m.QueenChangeHiveDetailComponent),
    resolve: {
      queenChangeHive: QueenChangeHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/queen-change-hive-update.component').then(m => m.QueenChangeHiveUpdateComponent),
    resolve: {
      queenChangeHive: QueenChangeHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/queen-change-hive-update.component').then(m => m.QueenChangeHiveUpdateComponent),
    resolve: {
      queenChangeHive: QueenChangeHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default queenChangeHiveRoute;
