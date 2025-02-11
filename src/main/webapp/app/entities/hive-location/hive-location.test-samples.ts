import dayjs from 'dayjs/esm';

import { IHiveLocation, NewHiveLocation } from './hive-location.model';

export const sampleWithRequiredData: IHiveLocation = {
  id: 8963,
  externalId: 168,
  uuid: 'prestigious wonderful',
  dateCreated: dayjs('2025-02-10T15:36'),
  dateModified: dayjs('2025-02-10T07:05'),
  dateSynched: dayjs('2025-02-10T07:45'),
};

export const sampleWithPartialData: IHiveLocation = {
  id: 1339,
  name: 'whereas',
  externalId: 5695,
  uuid: 'happy',
  dateCreated: dayjs('2025-02-10T01:50'),
  dateModified: dayjs('2025-02-10T12:18'),
  dateSynched: dayjs('2025-02-10T08:02'),
};

export const sampleWithFullData: IHiveLocation = {
  id: 32617,
  name: 'pile',
  externalId: 9955,
  uuid: 'yowza messy pry',
  dateCreated: dayjs('2025-02-10T12:09'),
  dateModified: dayjs('2025-02-10T02:45'),
  dateSynched: dayjs('2025-02-10T06:23'),
  dateDeleted: dayjs('2025-02-09T22:13'),
};

export const sampleWithNewData: NewHiveLocation = {
  externalId: 1772,
  uuid: 'unlawful',
  dateCreated: dayjs('2025-02-10T08:03'),
  dateModified: dayjs('2025-02-10T04:16'),
  dateSynched: dayjs('2025-02-10T16:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
