import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHive } from 'app/entities/hive/hive.model';

export interface IReproductionHive {
  id: number;
  note?: string | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  dateFinished?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  hive?: Pick<IHive, 'id'> | null;
}

export type NewReproductionHive = Omit<IReproductionHive, 'id'> & { id: null };
