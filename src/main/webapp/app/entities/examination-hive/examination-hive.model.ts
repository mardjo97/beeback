import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IExaminationHive {
  id: number;
  note?: string | null;
  dateExamination?: dayjs.Dayjs | null;
  reminderId?: string | null;
  externalId?: number | null;
  uuid?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  dateSynched?: dayjs.Dayjs | null;
  dateDeleted?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewExaminationHive = Omit<IExaminationHive, 'id'> & { id: null };
