import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IGroup {
  id: number;
  name?: string | null;
  enumValueName?: string | null;
  color?: string | null;
  hiveCount?: number | null;
  hiveCountFinished?: number | null;
  additionalInfo?: string | null;
  orderNumber?: number | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewGroup = Omit<IGroup, 'id'> & { id: null };
