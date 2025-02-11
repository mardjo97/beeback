import dayjs from 'dayjs/esm';

import { IGoodHarvestHive, NewGoodHarvestHive } from './good-harvest-hive.model';

export const sampleWithRequiredData: IGoodHarvestHive = {
  id: 7725,
  externalId: 2520,
  uuid: 'duh gerbil',
  dateCreated: dayjs('2025-02-10T19:11'),
  dateModified: dayjs('2025-02-10T01:56'),
  dateSynched: dayjs('2025-02-10T14:26'),
};

export const sampleWithPartialData: IGoodHarvestHive = {
  id: 11578,
  externalId: 1011,
  uuid: 'scarper',
  dateCreated: dayjs('2025-02-10T16:42'),
  dateModified: dayjs('2025-02-10T08:59'),
  dateSynched: dayjs('2025-02-10T15:26'),
  dateDeleted: dayjs('2025-02-10T12:57'),
};

export const sampleWithFullData: IGoodHarvestHive = {
  id: 5560,
  amount: 18424.16,
  externalId: 17360,
  uuid: 'emphasise amidst reproach',
  dateCreated: dayjs('2025-02-10T03:08'),
  dateModified: dayjs('2025-02-10T09:20'),
  dateSynched: dayjs('2025-02-10T17:39'),
  dateDeleted: dayjs('2025-02-10T17:11'),
};

export const sampleWithNewData: NewGoodHarvestHive = {
  externalId: 16003,
  uuid: 'edge shark',
  dateCreated: dayjs('2025-02-10T07:21'),
  dateModified: dayjs('2025-02-10T04:13'),
  dateSynched: dayjs('2025-02-10T17:39'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
