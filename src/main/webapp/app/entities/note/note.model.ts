import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHive } from 'app/entities/hive/hive.model';

export interface INote {
  id: number;
  hasReminder?: boolean | null;
  title?: string | null;
  content?: string | null;
  group?: string | null;
  groupRecordId?: number | null;
  dateHidden?: dayjs.Dayjs | null;
  reminderDate?: dayjs.Dayjs | null;
  reminderId?: number | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  hive?: Pick<IHive, 'id'> | null;
}

export type NewNote = Omit<INote, 'id'> & { id: null };
