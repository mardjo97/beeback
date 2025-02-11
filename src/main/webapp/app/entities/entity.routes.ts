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
  {
    path: 'hive-location',
    data: { pageTitle: 'beebackApp.hiveLocation.home.title' },
    loadChildren: () => import('./hive-location/hive-location.routes'),
  },
  {
    path: 'group',
    data: { pageTitle: 'beebackApp.group.home.title' },
    loadChildren: () => import('./group/group.routes'),
  },
  {
    path: 'moved-hive',
    data: { pageTitle: 'beebackApp.movedHive.home.title' },
    loadChildren: () => import('./moved-hive/moved-hive.routes'),
  },
  {
    path: 'queen-change-hive',
    data: { pageTitle: 'beebackApp.queenChangeHive.home.title' },
    loadChildren: () => import('./queen-change-hive/queen-change-hive.routes'),
  },
  {
    path: 'good-harvest-hive',
    data: { pageTitle: 'beebackApp.goodHarvestHive.home.title' },
    loadChildren: () => import('./good-harvest-hive/good-harvest-hive.routes'),
  },
  {
    path: 'reproduction-hive',
    data: { pageTitle: 'beebackApp.reproductionHive.home.title' },
    loadChildren: () => import('./reproduction-hive/reproduction-hive.routes'),
  },
  {
    path: 'examination-hive',
    data: { pageTitle: 'beebackApp.examinationHive.home.title' },
    loadChildren: () => import('./examination-hive/examination-hive.routes'),
  },
  {
    path: 'feeding-hive',
    data: { pageTitle: 'beebackApp.feedingHive.home.title' },
    loadChildren: () => import('./feeding-hive/feeding-hive.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
