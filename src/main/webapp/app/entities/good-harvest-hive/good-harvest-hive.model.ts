import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';

export interface IGoodHarvestHive {
  id: number;
  amount?: number | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  harvestType?: Pick<IHarvestType, 'id'> | null;
}

export type NewGoodHarvestHive = Omit<IGoodHarvestHive, 'id'> & { id: null };
