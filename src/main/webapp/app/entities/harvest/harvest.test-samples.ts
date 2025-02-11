import dayjs from 'dayjs/esm';

import { IHarvest, NewHarvest } from './harvest.model';

export const sampleWithRequiredData: IHarvest = {
  id: 21598,
  externalId: 21206,
  uuid: 'yippee',
  dateCreated: dayjs('2025-02-10T11:36'),
  dateModified: dayjs('2025-02-10T15:52'),
  dateSynched: dayjs('2025-02-10T06:21'),
};

export const sampleWithPartialData: IHarvest = {
  id: 25083,
  hiveFrames: 26067,
  dateCollected: dayjs('2025-02-09T20:55'),
  group: 'bah veto excepting',
  groupRecordId: 13851,
  externalId: 26214,
  uuid: 'oddball ah once',
  dateCreated: dayjs('2025-02-10T17:33'),
  dateModified: dayjs('2025-02-09T21:20'),
  dateSynched: dayjs('2025-02-10T12:25'),
  dateDeleted: dayjs('2025-02-10T10:58'),
};

export const sampleWithFullData: IHarvest = {
  id: 19724,
  hiveFrames: 28348,
  amount: 18254.02,
  dateCollected: dayjs('2025-02-10T18:22'),
  group: 'once via variable',
  groupRecordId: 19534,
  externalId: 3705,
  uuid: 'progress key',
  dateCreated: dayjs('2025-02-10T14:27'),
  dateModified: dayjs('2025-02-09T22:11'),
  dateSynched: dayjs('2025-02-10T09:24'),
  dateDeleted: dayjs('2025-02-10T18:32'),
};

export const sampleWithNewData: NewHarvest = {
  externalId: 30506,
  uuid: 'unto traffic whether',
  dateCreated: dayjs('2025-02-10T19:21'),
  dateModified: dayjs('2025-02-09T22:48'),
  dateSynched: dayjs('2025-02-10T07:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
