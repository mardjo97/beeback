import dayjs from 'dayjs/esm';

import { IQueen, NewQueen } from './queen.model';

export const sampleWithRequiredData: IQueen = {
  id: 21412,
  externalId: 27927,
  uuid: 'handsome elver',
  dateCreated: dayjs('2025-02-10T09:33'),
  dateModified: dayjs('2025-02-10T09:48'),
  dateSynched: dayjs('2025-02-10T00:31'),
};

export const sampleWithPartialData: IQueen = {
  id: 8022,
  origin: 'primary kiddingly',
  activeFromDate: dayjs('2025-02-10T03:25'),
  activeToDate: dayjs('2025-02-09T23:02'),
  queenChangeDate: dayjs('2025-02-10T02:00'),
  externalId: 4150,
  uuid: 'appropriate barring through',
  dateCreated: dayjs('2025-02-10T09:39'),
  dateModified: dayjs('2025-02-10T18:26'),
  dateSynched: dayjs('2025-02-10T14:29'),
};

export const sampleWithFullData: IQueen = {
  id: 6881,
  origin: 'anxiously',
  year: 8481,
  isMarked: false,
  active: true,
  activeFromDate: dayjs('2025-02-10T19:16'),
  activeToDate: dayjs('2025-02-10T18:03'),
  queenChangeDate: dayjs('2025-02-10T03:38'),
  externalId: 25201,
  uuid: 'bathhouse',
  dateCreated: dayjs('2025-02-10T13:52'),
  dateModified: dayjs('2025-02-10T18:44'),
  dateSynched: dayjs('2025-02-10T06:52'),
  dateDeleted: dayjs('2025-02-10T11:55'),
};

export const sampleWithNewData: NewQueen = {
  externalId: 21488,
  uuid: 'courtroom phooey',
  dateCreated: dayjs('2025-02-10T05:38'),
  dateModified: dayjs('2025-02-10T00:35'),
  dateSynched: dayjs('2025-02-10T07:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
