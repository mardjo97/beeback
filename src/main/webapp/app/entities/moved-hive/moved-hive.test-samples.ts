import dayjs from 'dayjs/esm';

import { IMovedHive, NewMovedHive } from './moved-hive.model';

export const sampleWithRequiredData: IMovedHive = {
  id: 2174,
  externalId: 7630,
  uuid: 'inasmuch aha secret',
  dateCreated: dayjs('2025-02-10T05:26'),
  dateModified: dayjs('2025-02-10T20:03'),
  dateSynched: dayjs('2025-02-10T07:08'),
};

export const sampleWithPartialData: IMovedHive = {
  id: 26634,
  externalId: 1086,
  uuid: 'deliberately arrange',
  dateCreated: dayjs('2025-02-10T17:22'),
  dateModified: dayjs('2025-02-10T11:20'),
  dateSynched: dayjs('2025-02-10T11:38'),
  dateDeleted: dayjs('2025-02-10T09:33'),
};

export const sampleWithFullData: IMovedHive = {
  id: 19854,
  location: 'accredit',
  externalId: 17151,
  uuid: 'across delectable grandpa',
  dateCreated: dayjs('2025-02-10T04:30'),
  dateModified: dayjs('2025-02-10T04:46'),
  dateSynched: dayjs('2025-02-10T10:24'),
  dateDeleted: dayjs('2025-02-10T14:09'),
};

export const sampleWithNewData: NewMovedHive = {
  externalId: 5100,
  uuid: 'an underneath rightfully',
  dateCreated: dayjs('2025-02-10T08:36'),
  dateModified: dayjs('2025-02-10T11:05'),
  dateSynched: dayjs('2025-02-10T07:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
