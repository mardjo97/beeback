import dayjs from 'dayjs/esm';

import { IGoodHarvestHive, NewGoodHarvestHive } from './good-harvest-hive.model';

export const sampleWithRequiredData: IGoodHarvestHive = {
  id: 28153,
  externalId: 17616,
  uuid: 'naughty afterwards fairly',
  dateCreated: dayjs('2025-02-09T21:02'),
  dateModified: dayjs('2025-02-10T12:06'),
  dateSynched: dayjs('2025-02-09T22:26'),
};

export const sampleWithPartialData: IGoodHarvestHive = {
  id: 18095,
  externalId: 29449,
  uuid: 'inside drowse accurate',
  dateCreated: dayjs('2025-02-09T23:56'),
  dateModified: dayjs('2025-02-09T23:53'),
  dateSynched: dayjs('2025-02-10T15:58'),
  dateFinished: dayjs('2025-02-09T23:09'),
};

export const sampleWithFullData: IGoodHarvestHive = {
  id: 32095,
  amount: 2714.77,
  externalId: 25022,
  uuid: 'finally',
  dateCreated: dayjs('2025-02-09T21:13'),
  dateModified: dayjs('2025-02-10T17:22'),
  dateSynched: dayjs('2025-02-09T23:44'),
  dateDeleted: dayjs('2025-02-10T19:39'),
  dateFinished: dayjs('2025-02-10T01:07'),
};

export const sampleWithNewData: NewGoodHarvestHive = {
  externalId: 25724,
  uuid: 'bar eek hungrily',
  dateCreated: dayjs('2025-02-10T11:08'),
  dateModified: dayjs('2025-02-10T17:32'),
  dateSynched: dayjs('2025-02-09T22:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
