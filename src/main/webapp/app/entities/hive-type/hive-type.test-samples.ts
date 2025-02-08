import dayjs from 'dayjs/esm';

import { IHiveType, NewHiveType } from './hive-type.model';

export const sampleWithRequiredData: IHiveType = {
  id: 15998,
  Name: 'as formamide',
  externalId: 18309,
  uuid: 'jovially obligation rapidly',
  dateCreated: dayjs('2025-01-11T02:48'),
  dateModified: dayjs('2025-01-10T18:24'),
  dateSynched: dayjs('2025-01-11T04:03'),
};

export const sampleWithPartialData: IHiveType = {
  id: 25230,
  Name: 'ascribe',
  externalId: 894,
  uuid: 'bathrobe debut thoroughly',
  dateCreated: dayjs('2025-01-10T17:32'),
  dateModified: dayjs('2025-01-11T05:23'),
  dateSynched: dayjs('2025-01-10T18:46'),
};

export const sampleWithFullData: IHiveType = {
  id: 20736,
  Name: 'though understanding underneath',
  externalId: 9348,
  uuid: 'yawningly clear-cut orchestrate',
  dateCreated: dayjs('2025-01-11T11:20'),
  dateModified: dayjs('2025-01-11T12:05'),
  dateSynched: dayjs('2025-01-11T00:01'),
  dateDeleted: dayjs('2025-01-11T10:16'),
};

export const sampleWithNewData: NewHiveType = {
  Name: 'spiteful gah',
  externalId: 24413,
  uuid: 'almighty disarm daily',
  dateCreated: dayjs('2025-01-11T10:10'),
  dateModified: dayjs('2025-01-10T12:39'),
  dateSynched: dayjs('2025-01-10T22:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
