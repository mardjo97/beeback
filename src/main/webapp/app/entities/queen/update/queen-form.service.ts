import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQueen, NewQueen } from '../queen.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQueen for edit and NewQueenFormGroupInput for create.
 */
type QueenFormGroupInput = IQueen | PartialWithRequiredKeyOf<NewQueen>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQueen | NewQueen> = Omit<
  T,
  'activeFromDate' | 'activeToDate' | 'queenChangeDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  activeFromDate?: string | null;
  activeToDate?: string | null;
  queenChangeDate?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type QueenFormRawValue = FormValueOf<IQueen>;

type NewQueenFormRawValue = FormValueOf<NewQueen>;

type QueenFormDefaults = Pick<
  NewQueen,
  | 'id'
  | 'isMarked'
  | 'active'
  | 'activeFromDate'
  | 'activeToDate'
  | 'queenChangeDate'
  | 'dateCreated'
  | 'dateModified'
  | 'dateSynched'
  | 'dateDeleted'
>;

type QueenFormGroupContent = {
  id: FormControl<QueenFormRawValue['id'] | NewQueen['id']>;
  origin: FormControl<QueenFormRawValue['origin']>;
  year: FormControl<QueenFormRawValue['year']>;
  isMarked: FormControl<QueenFormRawValue['isMarked']>;
  active: FormControl<QueenFormRawValue['active']>;
  activeFromDate: FormControl<QueenFormRawValue['activeFromDate']>;
  activeToDate: FormControl<QueenFormRawValue['activeToDate']>;
  queenChangeDate: FormControl<QueenFormRawValue['queenChangeDate']>;
  externalId: FormControl<QueenFormRawValue['externalId']>;
  uuid: FormControl<QueenFormRawValue['uuid']>;
  dateCreated: FormControl<QueenFormRawValue['dateCreated']>;
  dateModified: FormControl<QueenFormRawValue['dateModified']>;
  dateSynched: FormControl<QueenFormRawValue['dateSynched']>;
  dateDeleted: FormControl<QueenFormRawValue['dateDeleted']>;
  user: FormControl<QueenFormRawValue['user']>;
  hive: FormControl<QueenFormRawValue['hive']>;
};

export type QueenFormGroup = FormGroup<QueenFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QueenFormService {
  createQueenFormGroup(queen: QueenFormGroupInput = { id: null }): QueenFormGroup {
    const queenRawValue = this.convertQueenToQueenRawValue({
      ...this.getFormDefaults(),
      ...queen,
    });
    return new FormGroup<QueenFormGroupContent>({
      id: new FormControl(
        { value: queenRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      origin: new FormControl(queenRawValue.origin),
      year: new FormControl(queenRawValue.year),
      isMarked: new FormControl(queenRawValue.isMarked),
      active: new FormControl(queenRawValue.active),
      activeFromDate: new FormControl(queenRawValue.activeFromDate),
      activeToDate: new FormControl(queenRawValue.activeToDate),
      queenChangeDate: new FormControl(queenRawValue.queenChangeDate),
      externalId: new FormControl(queenRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(queenRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(queenRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(queenRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(queenRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(queenRawValue.dateDeleted),
      user: new FormControl(queenRawValue.user),
      hive: new FormControl(queenRawValue.hive),
    });
  }

  getQueen(form: QueenFormGroup): IQueen | NewQueen {
    return this.convertQueenRawValueToQueen(form.getRawValue() as QueenFormRawValue | NewQueenFormRawValue);
  }

  resetForm(form: QueenFormGroup, queen: QueenFormGroupInput): void {
    const queenRawValue = this.convertQueenToQueenRawValue({ ...this.getFormDefaults(), ...queen });
    form.reset(
      {
        ...queenRawValue,
        id: { value: queenRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QueenFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isMarked: false,
      active: false,
      activeFromDate: currentTime,
      activeToDate: currentTime,
      queenChangeDate: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertQueenRawValueToQueen(rawQueen: QueenFormRawValue | NewQueenFormRawValue): IQueen | NewQueen {
    return {
      ...rawQueen,
      activeFromDate: dayjs(rawQueen.activeFromDate, DATE_TIME_FORMAT),
      activeToDate: dayjs(rawQueen.activeToDate, DATE_TIME_FORMAT),
      queenChangeDate: dayjs(rawQueen.queenChangeDate, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawQueen.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawQueen.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawQueen.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawQueen.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertQueenToQueenRawValue(
    queen: IQueen | (Partial<NewQueen> & QueenFormDefaults),
  ): QueenFormRawValue | PartialWithRequiredKeyOf<NewQueenFormRawValue> {
    return {
      ...queen,
      activeFromDate: queen.activeFromDate ? queen.activeFromDate.format(DATE_TIME_FORMAT) : undefined,
      activeToDate: queen.activeToDate ? queen.activeToDate.format(DATE_TIME_FORMAT) : undefined,
      queenChangeDate: queen.queenChangeDate ? queen.queenChangeDate.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: queen.dateCreated ? queen.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: queen.dateModified ? queen.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: queen.dateSynched ? queen.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: queen.dateDeleted ? queen.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
