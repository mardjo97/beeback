import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IQueenChangeHive {
  id: number;
  dateQueenChange?: dayjs.Dayjs | null;
  reminderId?: string | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewQueenChangeHive = Omit<IQueenChangeHive, 'id'> & { id: null };
