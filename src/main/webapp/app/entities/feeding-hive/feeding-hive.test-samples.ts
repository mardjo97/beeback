import dayjs from 'dayjs/esm';

import { IFeedingHive, NewFeedingHive } from './feeding-hive.model';

export const sampleWithRequiredData: IFeedingHive = {
  id: 27883,
  externalId: 18729,
  uuid: 'cross fiercely aw',
  dateCreated: dayjs('2025-02-10T10:50'),
  dateModified: dayjs('2025-02-10T07:10'),
  dateSynched: dayjs('2025-02-09T22:15'),
};

export const sampleWithPartialData: IFeedingHive = {
  id: 9768,
  externalId: 19232,
  uuid: 'knowledgeably chilly',
  dateCreated: dayjs('2025-02-10T18:20'),
  dateModified: dayjs('2025-02-10T02:23'),
  dateSynched: dayjs('2025-02-10T17:29'),
  dateFinished: dayjs('2025-02-10T08:18'),
};

export const sampleWithFullData: IFeedingHive = {
  id: 8439,
  foodAmount: 14788.94,
  externalId: 3657,
  uuid: 'willfully',
  dateCreated: dayjs('2025-02-09T23:04'),
  dateModified: dayjs('2025-02-10T07:52'),
  dateSynched: dayjs('2025-02-10T12:44'),
  dateDeleted: dayjs('2025-02-10T13:01'),
  dateFinished: dayjs('2025-02-09T21:16'),
};

export const sampleWithNewData: NewFeedingHive = {
  externalId: 11254,
  uuid: 'safeguard',
  dateCreated: dayjs('2025-02-10T14:04'),
  dateModified: dayjs('2025-02-10T09:44'),
  dateSynched: dayjs('2025-02-10T16:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
