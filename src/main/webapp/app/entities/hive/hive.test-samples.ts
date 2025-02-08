import dayjs from 'dayjs/esm';

import { IHive, NewHive } from './hive.model';

export const sampleWithRequiredData: IHive = {
  id: 4724,
  barcode: 'eventually',
  externalId: 16662,
  uuid: 'effector alongside meanwhile',
  dateCreated: dayjs('2025-01-12T00:59'),
  dateModified: dayjs('2025-01-11T13:45'),
  dateSynched: dayjs('2025-01-12T09:49'),
};

export const sampleWithPartialData: IHive = {
  id: 17776,
  barcode: 'frantically over',
  orderNumber: 8086,
  description: 'duh conservation now',
  examinationDate: dayjs('2025-01-11T19:44'),
  archivedDate: dayjs('2025-01-12T08:34'),
  archivedReason: 'an likewise jut',
  externalId: 28550,
  uuid: 'extremely',
  dateCreated: dayjs('2025-01-11T15:08'),
  dateModified: dayjs('2025-01-12T10:18'),
  dateSynched: dayjs('2025-01-11T14:59'),
  dateDeleted: dayjs('2025-01-12T07:06'),
};

export const sampleWithFullData: IHive = {
  id: 31642,
  barcode: 'inasmuch',
  orderNumber: 4958,
  description: 'hopelessly',
  examinationDate: dayjs('2025-01-11T17:38'),
  archivedDate: dayjs('2025-01-11T19:39'),
  archivedReason: 'phew',
  externalId: 18743,
  uuid: 'scratch',
  dateCreated: dayjs('2025-01-12T00:48'),
  dateModified: dayjs('2025-01-11T12:54'),
  dateSynched: dayjs('2025-01-11T12:14'),
  dateDeleted: dayjs('2025-01-11T11:52'),
};

export const sampleWithNewData: NewHive = {
  barcode: 'phew making inasmuch',
  externalId: 27944,
  uuid: 'like',
  dateCreated: dayjs('2025-01-11T16:24'),
  dateModified: dayjs('2025-01-12T09:50'),
  dateSynched: dayjs('2025-01-12T02:59'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
