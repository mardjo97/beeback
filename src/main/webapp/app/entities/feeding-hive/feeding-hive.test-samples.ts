import dayjs from 'dayjs/esm';

import { IFeedingHive, NewFeedingHive } from './feeding-hive.model';

export const sampleWithRequiredData: IFeedingHive = {
  id: 5655,
  externalId: 12469,
  uuid: 'stylish',
  dateCreated: dayjs('2025-02-09T21:54'),
  dateModified: dayjs('2025-02-10T12:47'),
  dateSynched: dayjs('2025-02-10T19:28'),
};

export const sampleWithPartialData: IFeedingHive = {
  id: 26507,
  foodAmount: 29428.01,
  externalId: 19657,
  uuid: 'relative',
  dateCreated: dayjs('2025-02-10T03:24'),
  dateModified: dayjs('2025-02-10T08:46'),
  dateSynched: dayjs('2025-02-10T10:50'),
};

export const sampleWithFullData: IFeedingHive = {
  id: 15137,
  foodAmount: 2951.66,
  externalId: 22328,
  uuid: 'bah heavy swat',
  dateCreated: dayjs('2025-02-10T08:18'),
  dateModified: dayjs('2025-02-10T02:16'),
  dateSynched: dayjs('2025-02-10T06:55'),
  dateDeleted: dayjs('2025-02-09T22:46'),
};

export const sampleWithNewData: NewFeedingHive = {
  externalId: 1602,
  uuid: 'amidst indeed',
  dateCreated: dayjs('2025-02-09T21:12'),
  dateModified: dayjs('2025-02-10T01:55'),
  dateSynched: dayjs('2025-02-10T16:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
