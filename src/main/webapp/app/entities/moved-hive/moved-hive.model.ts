import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHive } from 'app/entities/hive/hive.model';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';

export interface IMovedHive {
  id: number;
  location?: string | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  hive?: Pick<IHive, 'id'> | null;
  harvestType?: Pick<IHarvestType, 'id'> | null;
}

export type NewMovedHive = Omit<IMovedHive, 'id'> & { id: null };
