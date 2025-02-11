import dayjs from 'dayjs/esm';

import { IAppConfig, NewAppConfig } from './app-config.model';

export const sampleWithRequiredData: IAppConfig = {
  id: 15776,
  externalId: 21232,
  uuid: 'guzzle orderly',
  dateCreated: dayjs('2025-02-10T11:44'),
  dateModified: dayjs('2025-02-09T20:10'),
  dateSynched: dayjs('2025-02-10T05:15'),
};

export const sampleWithPartialData: IAppConfig = {
  id: 11264,
  value: 'frizzy',
  externalId: 9979,
  uuid: 'save',
  dateCreated: dayjs('2025-02-10T12:21'),
  dateModified: dayjs('2025-02-10T05:24'),
  dateSynched: dayjs('2025-02-10T01:03'),
  dateDeleted: dayjs('2025-02-09T22:08'),
};

export const sampleWithFullData: IAppConfig = {
  id: 15349,
  key: 'trim',
  type: 'almost successfully spectacles',
  value: 'bitterly spherical',
  externalId: 23676,
  uuid: 'horn offensively',
  dateCreated: dayjs('2025-02-10T14:29'),
  dateModified: dayjs('2025-02-10T19:03'),
  dateSynched: dayjs('2025-02-10T03:54'),
  dateDeleted: dayjs('2025-02-10T09:43'),
};

export const sampleWithNewData: NewAppConfig = {
  externalId: 27780,
  uuid: 'whereas',
  dateCreated: dayjs('2025-02-10T14:51'),
  dateModified: dayjs('2025-02-10T19:57'),
  dateSynched: dayjs('2025-02-10T05:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
