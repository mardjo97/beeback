import dayjs from 'dayjs/esm';

import { IReproductionHive, NewReproductionHive } from './reproduction-hive.model';

export const sampleWithRequiredData: IReproductionHive = {
  id: 17241,
  externalId: 16517,
  uuid: 'wilt',
  dateCreated: dayjs('2025-02-10T01:44'),
  dateModified: dayjs('2025-02-10T02:38'),
  dateSynched: dayjs('2025-02-09T22:56'),
};

export const sampleWithPartialData: IReproductionHive = {
  id: 4504,
  externalId: 12016,
  uuid: 'pace decision free',
  dateCreated: dayjs('2025-02-09T22:29'),
  dateModified: dayjs('2025-02-10T06:51'),
  dateSynched: dayjs('2025-02-10T00:50'),
  dateDeleted: dayjs('2025-02-10T18:16'),
};

export const sampleWithFullData: IReproductionHive = {
  id: 23974,
  note: 'frantically colorless mixture',
  externalId: 32112,
  uuid: 'perfection ew recount',
  dateCreated: dayjs('2025-02-10T06:29'),
  dateModified: dayjs('2025-02-10T03:52'),
  dateSynched: dayjs('2025-02-09T20:19'),
  dateDeleted: dayjs('2025-02-10T19:26'),
  dateFinished: dayjs('2025-02-10T09:07'),
};

export const sampleWithNewData: NewReproductionHive = {
  externalId: 32663,
  uuid: 'founder',
  dateCreated: dayjs('2025-02-10T03:21'),
  dateModified: dayjs('2025-02-10T13:15'),
  dateSynched: dayjs('2025-02-10T11:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
