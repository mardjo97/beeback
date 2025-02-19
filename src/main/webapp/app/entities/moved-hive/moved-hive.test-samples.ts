import dayjs from 'dayjs/esm';

import { IMovedHive, NewMovedHive } from './moved-hive.model';

export const sampleWithRequiredData: IMovedHive = {
  id: 15913,
  externalId: 4403,
  uuid: 'aha thorn',
  dateCreated: dayjs('2025-02-10T02:33'),
  dateModified: dayjs('2025-02-10T15:36'),
  dateSynched: dayjs('2025-02-09T20:53'),
};

export const sampleWithPartialData: IMovedHive = {
  id: 17807,
  location: 'phooey',
  externalId: 27927,
  uuid: 'coincide accredit',
  dateCreated: dayjs('2025-02-10T08:39'),
  dateModified: dayjs('2025-02-10T18:18'),
  dateSynched: dayjs('2025-02-09T23:12'),
  dateFinished: dayjs('2025-02-10T09:43'),
};

export const sampleWithFullData: IMovedHive = {
  id: 12319,
  location: 'ha colon',
  externalId: 14702,
  uuid: 'validity an',
  dateCreated: dayjs('2025-02-10T19:43'),
  dateModified: dayjs('2025-02-10T03:21'),
  dateSynched: dayjs('2025-02-10T19:17'),
  dateDeleted: dayjs('2025-02-10T12:31'),
  dateFinished: dayjs('2025-02-10T02:38'),
};

export const sampleWithNewData: NewMovedHive = {
  externalId: 7127,
  uuid: 'rightfully publication bludgeon',
  dateCreated: dayjs('2025-02-10T09:40'),
  dateModified: dayjs('2025-02-10T10:43'),
  dateSynched: dayjs('2025-02-10T17:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
