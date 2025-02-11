import dayjs from 'dayjs/esm';

import { INote, NewNote } from './note.model';

export const sampleWithRequiredData: INote = {
  id: 18824,
  externalId: 32726,
  uuid: 'yearningly',
  dateCreated: dayjs('2025-02-10T03:56'),
  dateModified: dayjs('2025-02-09T22:57'),
  dateSynched: dayjs('2025-02-10T01:55'),
};

export const sampleWithPartialData: INote = {
  id: 20373,
  title: 'academics unless condense',
  content: 'similar nervously',
  dateHidden: dayjs('2025-02-10T08:46'),
  externalId: 29201,
  uuid: 'round',
  dateCreated: dayjs('2025-02-10T00:04'),
  dateModified: dayjs('2025-02-10T10:33'),
  dateSynched: dayjs('2025-02-10T18:45'),
};

export const sampleWithFullData: INote = {
  id: 7289,
  hasReminder: false,
  title: 'blah inveigle',
  content: 'drat unsteady',
  group: 'aware',
  groupRecordId: 3661,
  dateHidden: dayjs('2025-02-10T02:50'),
  reminderDate: dayjs('2025-02-10T13:52'),
  reminderId: 5083,
  externalId: 26875,
  uuid: 'gee apropos however',
  dateCreated: dayjs('2025-02-09T22:49'),
  dateModified: dayjs('2025-02-10T14:25'),
  dateSynched: dayjs('2025-02-09T21:16'),
  dateDeleted: dayjs('2025-02-10T05:20'),
};

export const sampleWithNewData: NewNote = {
  externalId: 27687,
  uuid: 'heighten waterlogged gadzooks',
  dateCreated: dayjs('2025-02-10T11:41'),
  dateModified: dayjs('2025-02-09T22:41'),
  dateSynched: dayjs('2025-02-10T04:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
