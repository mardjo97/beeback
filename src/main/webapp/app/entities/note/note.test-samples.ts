import dayjs from 'dayjs/esm';

import { INote, NewNote } from './note.model';

export const sampleWithRequiredData: INote = {
  id: 19591,
  externalId: 30222,
  uuid: 'parched continually so',
  dateCreated: dayjs('2025-02-10T06:26'),
  dateModified: dayjs('2025-02-10T00:14'),
  dateSynched: dayjs('2025-02-10T09:57'),
};

export const sampleWithPartialData: INote = {
  id: 13164,
  hasReminder: true,
  group: 'husk',
  externalId: 3607,
  uuid: 'necklace usher',
  dateCreated: dayjs('2025-02-10T02:01'),
  dateModified: dayjs('2025-02-09T22:19'),
  dateSynched: dayjs('2025-02-10T12:24'),
};

export const sampleWithFullData: INote = {
  id: 5473,
  hasReminder: true,
  title: 'through hmph',
  content: 'patiently',
  group: 'implode amidst poor',
  groupRecordId: 25720,
  dateHidden: dayjs('2025-02-09T20:17'),
  reminderDate: dayjs('2025-02-10T16:35'),
  reminderId: 5009,
  externalId: 24867,
  uuid: 'meh marvelous plus',
  dateCreated: dayjs('2025-02-10T06:25'),
  dateModified: dayjs('2025-02-10T17:23'),
  dateSynched: dayjs('2025-02-10T13:59'),
};

export const sampleWithNewData: NewNote = {
  externalId: 4123,
  uuid: 'pish before now',
  dateCreated: dayjs('2025-02-10T16:47'),
  dateModified: dayjs('2025-02-10T13:35'),
  dateSynched: dayjs('2025-02-10T01:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
