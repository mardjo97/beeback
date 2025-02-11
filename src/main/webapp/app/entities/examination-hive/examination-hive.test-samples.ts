import dayjs from 'dayjs/esm';

import { IExaminationHive, NewExaminationHive } from './examination-hive.model';

export const sampleWithRequiredData: IExaminationHive = {
  id: 23983,
  externalId: 7037,
  uuid: 'amongst treasure mouser',
  dateCreated: dayjs('2025-02-10T00:05'),
  dateModified: dayjs('2025-02-10T18:35'),
  dateSynched: dayjs('2025-02-09T21:11'),
};

export const sampleWithPartialData: IExaminationHive = {
  id: 10170,
  reminderId: 'carouse likewise',
  externalId: 23234,
  uuid: 'tenderly surprise hm',
  dateCreated: dayjs('2025-02-10T19:36'),
  dateModified: dayjs('2025-02-10T01:20'),
  dateSynched: dayjs('2025-02-10T00:22'),
  dateDeleted: dayjs('2025-02-10T04:12'),
};

export const sampleWithFullData: IExaminationHive = {
  id: 11691,
  note: 'beyond pine',
  dateExamination: dayjs('2025-02-10T07:01'),
  reminderId: 'readies within velocity',
  externalId: 12773,
  uuid: 'ha',
  dateCreated: dayjs('2025-02-10T11:31'),
  dateModified: dayjs('2025-02-10T00:51'),
  dateSynched: dayjs('2025-02-10T16:36'),
  dateDeleted: dayjs('2025-02-10T15:35'),
};

export const sampleWithNewData: NewExaminationHive = {
  externalId: 15461,
  uuid: 'vice scramble',
  dateCreated: dayjs('2025-02-10T16:57'),
  dateModified: dayjs('2025-02-10T18:53'),
  dateSynched: dayjs('2025-02-09T20:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
