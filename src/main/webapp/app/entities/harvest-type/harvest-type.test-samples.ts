import dayjs from 'dayjs/esm';

import { IHarvestType, NewHarvestType } from './harvest-type.model';

export const sampleWithRequiredData: IHarvestType = {
  id: 31914,
  externalId: 7997,
  uuid: 'that',
  dateCreated: dayjs('2025-02-10T16:54'),
  dateModified: dayjs('2025-02-09T22:37'),
  dateSynched: dayjs('2025-02-10T06:03'),
};

export const sampleWithPartialData: IHarvestType = {
  id: 16416,
  name: 'granny bran like',
  externalId: 13062,
  uuid: 'rusty degrease',
  dateCreated: dayjs('2025-02-10T02:16'),
  dateModified: dayjs('2025-02-09T22:07'),
  dateSynched: dayjs('2025-02-10T09:43'),
  dateDeleted: dayjs('2025-02-10T11:41'),
};

export const sampleWithFullData: IHarvestType = {
  id: 24501,
  name: 'and vacation fooey',
  externalId: 28965,
  uuid: 'whoever gadzooks',
  dateCreated: dayjs('2025-02-10T03:53'),
  dateModified: dayjs('2025-02-10T08:19'),
  dateSynched: dayjs('2025-02-10T10:48'),
  dateDeleted: dayjs('2025-02-10T16:01'),
};

export const sampleWithNewData: NewHarvestType = {
  externalId: 21792,
  uuid: 'corny refine nor',
  dateCreated: dayjs('2025-02-10T06:08'),
  dateModified: dayjs('2025-02-10T19:16'),
  dateSynched: dayjs('2025-02-10T13:15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
