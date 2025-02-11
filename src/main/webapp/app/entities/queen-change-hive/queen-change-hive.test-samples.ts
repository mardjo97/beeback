import dayjs from 'dayjs/esm';

import { IQueenChangeHive, NewQueenChangeHive } from './queen-change-hive.model';

export const sampleWithRequiredData: IQueenChangeHive = {
  id: 24141,
  externalId: 13565,
  uuid: 'upside-down until unsung',
  dateCreated: dayjs('2025-02-10T11:54'),
  dateModified: dayjs('2025-02-10T11:16'),
  dateSynched: dayjs('2025-02-09T23:09'),
};

export const sampleWithPartialData: IQueenChangeHive = {
  id: 1612,
  reminderId: 'brilliant oof fooey',
  externalId: 23163,
  uuid: 'ouch woot',
  dateCreated: dayjs('2025-02-10T07:21'),
  dateModified: dayjs('2025-02-10T19:46'),
  dateSynched: dayjs('2025-02-10T08:27'),
  dateDeleted: dayjs('2025-02-10T02:08'),
};

export const sampleWithFullData: IQueenChangeHive = {
  id: 19237,
  dateQueenChange: dayjs('2025-02-09T22:13'),
  reminderId: 'snoopy yearly',
  externalId: 5990,
  uuid: 'yearly nephew',
  dateCreated: dayjs('2025-02-10T18:08'),
  dateModified: dayjs('2025-02-10T11:31'),
  dateSynched: dayjs('2025-02-10T15:24'),
  dateDeleted: dayjs('2025-02-10T08:51'),
};

export const sampleWithNewData: NewQueenChangeHive = {
  externalId: 7205,
  uuid: 'anenst gastropod poorly',
  dateCreated: dayjs('2025-02-09T20:41'),
  dateModified: dayjs('2025-02-10T07:00'),
  dateSynched: dayjs('2025-02-10T00:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
