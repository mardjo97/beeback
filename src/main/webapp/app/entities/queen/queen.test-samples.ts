import dayjs from 'dayjs/esm';

import { IQueen, NewQueen } from './queen.model';

export const sampleWithRequiredData: IQueen = {
  id: 3096,
  externalId: 247,
  uuid: 'inasmuch',
  dateCreated: dayjs('2025-02-09T21:56'),
  dateModified: dayjs('2025-02-10T01:13'),
  dateSynched: dayjs('2025-02-10T17:06'),
};

export const sampleWithPartialData: IQueen = {
  id: 21429,
  origin: 'wrongly supposing proselytise',
  year: 22631,
  isMarked: true,
  activeFromDate: dayjs('2025-02-10T17:21'),
  activeToDate: dayjs('2025-02-10T16:24'),
  queenChangeDate: dayjs('2025-02-10T10:16'),
  externalId: 27818,
  uuid: 'butter vice with',
  dateCreated: dayjs('2025-02-09T20:09'),
  dateModified: dayjs('2025-02-10T11:51'),
  dateSynched: dayjs('2025-02-10T12:19'),
};

export const sampleWithFullData: IQueen = {
  id: 15050,
  origin: 'tectonics',
  year: 30950,
  isMarked: true,
  active: true,
  activeFromDate: dayjs('2025-02-10T03:17'),
  activeToDate: dayjs('2025-02-10T17:18'),
  queenChangeDate: dayjs('2025-02-10T01:58'),
  externalId: 19375,
  uuid: 'steep greatly than',
  dateCreated: dayjs('2025-02-10T17:33'),
  dateModified: dayjs('2025-02-10T01:30'),
  dateSynched: dayjs('2025-02-10T06:26'),
};

export const sampleWithNewData: NewQueen = {
  externalId: 1150,
  uuid: 'daily membership',
  dateCreated: dayjs('2025-02-10T18:26'),
  dateModified: dayjs('2025-02-10T14:29'),
  dateSynched: dayjs('2025-02-10T01:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
