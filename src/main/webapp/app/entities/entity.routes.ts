import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'beebackApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'apiary',
    data: { pageTitle: 'beebackApp.apiary.home.title' },
    loadChildren: () => import('./apiary/apiary.routes'),
  },
  {
    path: 'hive-type',
    data: { pageTitle: 'beebackApp.hiveType.home.title' },
    loadChildren: () => import('./hive-type/hive-type.routes'),
  },
  {
    path: 'hive',
    data: { pageTitle: 'beebackApp.hive.home.title' },
    loadChildren: () => import('./hive/hive.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
