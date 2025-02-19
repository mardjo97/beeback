import dayjs from 'dayjs/esm';

import { IQueenChangeHive, NewQueenChangeHive } from './queen-change-hive.model';

export const sampleWithRequiredData: IQueenChangeHive = {
  id: 29107,
  externalId: 23838,
  uuid: 'yawningly compete',
  dateCreated: dayjs('2025-02-10T09:19'),
  dateModified: dayjs('2025-02-09T20:23'),
  dateSynched: dayjs('2025-02-10T17:46'),
};

export const sampleWithPartialData: IQueenChangeHive = {
  id: 1209,
  dateQueenChange: dayjs('2025-02-09T22:39'),
  externalId: 20412,
  uuid: 'closely',
  dateCreated: dayjs('2025-02-10T03:00'),
  dateModified: dayjs('2025-02-10T06:29'),
  dateSynched: dayjs('2025-02-10T07:21'),
  dateDeleted: dayjs('2025-02-10T19:46'),
  dateFinished: dayjs('2025-02-10T08:27'),
};

export const sampleWithFullData: IQueenChangeHive = {
  id: 8256,
  dateQueenChange: dayjs('2025-02-10T10:11'),
  reminderId: 'or',
  externalId: 25117,
  uuid: 'finally effector',
  dateCreated: dayjs('2025-02-10T19:46'),
  dateModified: dayjs('2025-02-10T02:26'),
  dateSynched: dayjs('2025-02-10T01:31'),
  dateDeleted: dayjs('2025-02-10T11:26'),
  dateFinished: dayjs('2025-02-10T05:34'),
};

export const sampleWithNewData: NewQueenChangeHive = {
  externalId: 8814,
  uuid: 'why',
  dateCreated: dayjs('2025-02-09T20:51'),
  dateModified: dayjs('2025-02-10T03:43'),
  dateSynched: dayjs('2025-02-10T00:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
