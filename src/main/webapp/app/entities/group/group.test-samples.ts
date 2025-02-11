import dayjs from 'dayjs/esm';

import { IGroup, NewGroup } from './group.model';

export const sampleWithRequiredData: IGroup = {
  id: 66,
  externalId: 26305,
  uuid: 'optimistically',
  dateCreated: dayjs('2025-02-10T08:48'),
  dateModified: dayjs('2025-02-10T15:14'),
  dateSynched: dayjs('2025-02-10T18:11'),
};

export const sampleWithPartialData: IGroup = {
  id: 20618,
  enumValueName: 'next frightfully',
  color: 'orchid',
  hiveCountFinished: 4045,
  additionalInfo: 'decode thoroughly fragrant',
  orderNumber: 29096,
  externalId: 26503,
  uuid: 'that',
  dateCreated: dayjs('2025-02-09T21:09'),
  dateModified: dayjs('2025-02-10T03:27'),
  dateSynched: dayjs('2025-02-09T21:03'),
  dateDeleted: dayjs('2025-02-10T00:19'),
};

export const sampleWithFullData: IGroup = {
  id: 14110,
  name: 'toward reborn siege',
  enumValueName: 'compromise bossy',
  color: 'black',
  hiveCount: 22330,
  hiveCountFinished: 3800,
  additionalInfo: 'cheese corny',
  orderNumber: 9539,
  externalId: 4683,
  uuid: 'loudly minus',
  dateCreated: dayjs('2025-02-10T19:24'),
  dateModified: dayjs('2025-02-09T22:45'),
  dateSynched: dayjs('2025-02-10T15:59'),
  dateDeleted: dayjs('2025-02-10T16:13'),
};

export const sampleWithNewData: NewGroup = {
  externalId: 27093,
  uuid: 'around fondly potentially',
  dateCreated: dayjs('2025-02-10T01:27'),
  dateModified: dayjs('2025-02-10T08:16'),
  dateSynched: dayjs('2025-02-10T05:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
