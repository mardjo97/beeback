import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IApiary {
  id: number;
  name?: string | null;
  idNumber?: string | null;
  color?: string | null;
  location?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  orderNumber?: number | null;
  hiveCount?: number | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewApiary = Omit<IApiary, 'id'> & { id: null };
