import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExaminationHiveResolve from './route/examination-hive-routing-resolve.service';

const examinationHiveRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/examination-hive.component').then(m => m.ExaminationHiveComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/examination-hive-detail.component').then(m => m.ExaminationHiveDetailComponent),
    resolve: {
      examinationHive: ExaminationHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/examination-hive-update.component').then(m => m.ExaminationHiveUpdateComponent),
    resolve: {
      examinationHive: ExaminationHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/examination-hive-update.component').then(m => m.ExaminationHiveUpdateComponent),
    resolve: {
      examinationHive: ExaminationHiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default examinationHiveRoute;
