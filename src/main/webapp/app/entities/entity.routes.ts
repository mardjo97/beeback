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
  {
    path: 'queen',
    data: { pageTitle: 'beebackApp.queen.home.title' },
    loadChildren: () => import('./queen/queen.routes'),
  },
  {
    path: 'note',
    data: { pageTitle: 'beebackApp.note.home.title' },
    loadChildren: () => import('./note/note.routes'),
  },
  {
    path: 'harvest-type',
    data: { pageTitle: 'beebackApp.harvestType.home.title' },
    loadChildren: () => import('./harvest-type/harvest-type.routes'),
  },
  {
    path: 'harvest',
    data: { pageTitle: 'beebackApp.harvest.home.title' },
    loadChildren: () => import('./harvest/harvest.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
