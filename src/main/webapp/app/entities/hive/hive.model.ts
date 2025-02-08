import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHiveType } from 'app/entities/hive-type/hive-type.model';
import { IApiary } from 'app/entities/apiary/apiary.model';

export interface IHive {
  id: number;
  barcode?: string | null;
  orderNumber?: number | null;
  description?: string | null;
  examinationDate?: dayjs.Dayjs | null;
  archivedDate?: dayjs.Dayjs | null;
  archivedReason?: string | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  hiveType?: Pick<IHiveType, 'id'> | null;
  apiary?: Pick<IApiary, 'id'> | null;
}

export type NewHive = Omit<IHive, 'id'> & { id: null };
