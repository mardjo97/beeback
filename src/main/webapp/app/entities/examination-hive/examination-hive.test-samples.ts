import dayjs from 'dayjs/esm';

import { IExaminationHive, NewExaminationHive } from './examination-hive.model';

export const sampleWithRequiredData: IExaminationHive = {
  id: 20720,
  externalId: 5387,
  uuid: 'upbeat',
  dateCreated: dayjs('2025-02-10T12:03'),
  dateModified: dayjs('2025-02-09T22:11'),
  dateSynched: dayjs('2025-02-09T23:37'),
};

export const sampleWithPartialData: IExaminationHive = {
  id: 17625,
  note: 'behind except finally',
  reminderId: 'approximate',
  externalId: 17138,
  uuid: 'moisten beyond pine',
  dateCreated: dayjs('2025-02-10T07:01'),
  dateModified: dayjs('2025-02-10T15:12'),
  dateSynched: dayjs('2025-02-10T03:01'),
};

export const sampleWithFullData: IExaminationHive = {
  id: 3806,
  note: 'programme out wherever',
  dateExamination: dayjs('2025-02-10T07:02'),
  reminderId: 'hmph',
  externalId: 26614,
  uuid: 'futon athwart',
  dateCreated: dayjs('2025-02-10T14:41'),
  dateModified: dayjs('2025-02-10T16:57'),
  dateSynched: dayjs('2025-02-10T18:53'),
  dateDeleted: dayjs('2025-02-09T20:12'),
  dateFinished: dayjs('2025-02-10T17:42'),
};

export const sampleWithNewData: NewExaminationHive = {
  externalId: 12921,
  uuid: 'oily or criminal',
  dateCreated: dayjs('2025-02-10T06:06'),
  dateModified: dayjs('2025-02-10T10:37'),
  dateSynched: dayjs('2025-02-10T00:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
