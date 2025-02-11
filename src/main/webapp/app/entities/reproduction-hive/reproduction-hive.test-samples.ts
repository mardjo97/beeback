import dayjs from 'dayjs/esm';

import { IReproductionHive, NewReproductionHive } from './reproduction-hive.model';

export const sampleWithRequiredData: IReproductionHive = {
  id: 4154,
  externalId: 28845,
  uuid: 'testify perfection',
  dateCreated: dayjs('2025-02-10T18:22'),
  dateModified: dayjs('2025-02-10T14:20'),
  dateSynched: dayjs('2025-02-10T03:15'),
};

export const sampleWithPartialData: IReproductionHive = {
  id: 27220,
  externalId: 8809,
  uuid: 'hospitalization nicely',
  dateCreated: dayjs('2025-02-10T06:51'),
  dateModified: dayjs('2025-02-10T19:12'),
  dateSynched: dayjs('2025-02-10T05:52'),
  dateDeleted: dayjs('2025-02-10T17:18'),
};

export const sampleWithFullData: IReproductionHive = {
  id: 12111,
  note: 'aside wolf',
  externalId: 30289,
  uuid: 'creaking blah',
  dateCreated: dayjs('2025-02-10T13:14'),
  dateModified: dayjs('2025-02-10T11:02'),
  dateSynched: dayjs('2025-02-10T08:02'),
  dateDeleted: dayjs('2025-02-10T15:54'),
};

export const sampleWithNewData: NewReproductionHive = {
  externalId: 17881,
  uuid: 'likable respectful',
  dateCreated: dayjs('2025-02-10T01:22'),
  dateModified: dayjs('2025-02-10T03:21'),
  dateSynched: dayjs('2025-02-10T05:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
