import dayjs from 'dayjs/esm';

import { IApiary, NewApiary } from './apiary.model';

export const sampleWithRequiredData: IApiary = {
  id: 9046,
  externalId: 16204,
  uuid: 'vista',
  dateCreated: dayjs('2024-11-12T21:37'),
  dateModified: dayjs('2024-11-13T06:51'),
  dateSynched: dayjs('2024-11-13T05:41'),
};

export const sampleWithPartialData: IApiary = {
  id: 24698,
  name: 'midst phooey phooey',
  idNumber: 'forgo swim',
  longitude: 31377.04,
  orderNumber: 21359,
  externalId: 17086,
  uuid: 'wilted iterate gah',
  dateCreated: dayjs('2024-11-12T21:37'),
  dateModified: dayjs('2024-11-12T21:07'),
  dateSynched: dayjs('2024-11-13T16:25'),
};

export const sampleWithFullData: IApiary = {
  id: 12875,
  name: 'grouper',
  idNumber: 'zowie',
  color: 'sky blue',
  location: 'violently upon',
  latitude: 9483.08,
  longitude: 9621.65,
  orderNumber: 11555,
  hiveCount: 19517,
  externalId: 10467,
  uuid: 'gosh neglect mockingly',
  dateCreated: dayjs('2024-11-12T23:12'),
  dateModified: dayjs('2024-11-13T13:38'),
  dateSynched: dayjs('2024-11-12T21:18'),
};

export const sampleWithNewData: NewApiary = {
  externalId: 9794,
  uuid: 'psst before conclude',
  dateCreated: dayjs('2024-11-13T12:10'),
  dateModified: dayjs('2024-11-13T01:09'),
  dateSynched: dayjs('2024-11-13T12:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
