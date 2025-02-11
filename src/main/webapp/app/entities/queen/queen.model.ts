import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHive } from 'app/entities/hive/hive.model';

export interface IQueen {
  id: number;
  origin?: string | null;
  year?: number | null;
  isMarked?: boolean | null;
  active?: boolean | null;
  activeFromDate?: dayjs.Dayjs | null;
  activeToDate?: dayjs.Dayjs | null;
  queenChangeDate?: dayjs.Dayjs | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  hive?: Pick<IHive, 'id'> | null;
}

export type NewQueen = Omit<IQueen, 'id'> & { id: null };
