import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQueenChangeHive, NewQueenChangeHive } from '../queen-change-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQueenChangeHive for edit and NewQueenChangeHiveFormGroupInput for create.
 */
type QueenChangeHiveFormGroupInput = IQueenChangeHive | PartialWithRequiredKeyOf<NewQueenChangeHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQueenChangeHive | NewQueenChangeHive> = Omit<
  T,
  'dateQueenChange' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
> & {
  dateQueenChange?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
  dateFinished?: string | null;
};

type QueenChangeHiveFormRawValue = FormValueOf<IQueenChangeHive>;

type NewQueenChangeHiveFormRawValue = FormValueOf<NewQueenChangeHive>;

type QueenChangeHiveFormDefaults = Pick<
  NewQueenChangeHive,
  'id' | 'dateQueenChange' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
>;

type QueenChangeHiveFormGroupContent = {
  id: FormControl<QueenChangeHiveFormRawValue['id'] | NewQueenChangeHive['id']>;
  dateQueenChange: FormControl<QueenChangeHiveFormRawValue['dateQueenChange']>;
  reminderId: FormControl<QueenChangeHiveFormRawValue['reminderId']>;
  externalId: FormControl<QueenChangeHiveFormRawValue['externalId']>;
  uuid: FormControl<QueenChangeHiveFormRawValue['uuid']>;
  dateCreated: FormControl<QueenChangeHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<QueenChangeHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<QueenChangeHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<QueenChangeHiveFormRawValue['dateDeleted']>;
  dateFinished: FormControl<QueenChangeHiveFormRawValue['dateFinished']>;
  user: FormControl<QueenChangeHiveFormRawValue['user']>;
  hive: FormControl<QueenChangeHiveFormRawValue['hive']>;
};

export type QueenChangeHiveFormGroup = FormGroup<QueenChangeHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QueenChangeHiveFormService {
  createQueenChangeHiveFormGroup(queenChangeHive: QueenChangeHiveFormGroupInput = { id: null }): QueenChangeHiveFormGroup {
    const queenChangeHiveRawValue = this.convertQueenChangeHiveToQueenChangeHiveRawValue({
      ...this.getFormDefaults(),
      ...queenChangeHive,
    });
    return new FormGroup<QueenChangeHiveFormGroupContent>({
      id: new FormControl(
        { value: queenChangeHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateQueenChange: new FormControl(queenChangeHiveRawValue.dateQueenChange),
      reminderId: new FormControl(queenChangeHiveRawValue.reminderId),
      externalId: new FormControl(queenChangeHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(queenChangeHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(queenChangeHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(queenChangeHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(queenChangeHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(queenChangeHiveRawValue.dateDeleted),
      dateFinished: new FormControl(queenChangeHiveRawValue.dateFinished),
      user: new FormControl(queenChangeHiveRawValue.user),
      hive: new FormControl(queenChangeHiveRawValue.hive),
    });
  }

  getQueenChangeHive(form: QueenChangeHiveFormGroup): IQueenChangeHive | NewQueenChangeHive {
    return this.convertQueenChangeHiveRawValueToQueenChangeHive(
      form.getRawValue() as QueenChangeHiveFormRawValue | NewQueenChangeHiveFormRawValue,
    );
  }

  resetForm(form: QueenChangeHiveFormGroup, queenChangeHive: QueenChangeHiveFormGroupInput): void {
    const queenChangeHiveRawValue = this.convertQueenChangeHiveToQueenChangeHiveRawValue({ ...this.getFormDefaults(), ...queenChangeHive });
    form.reset(
      {
        ...queenChangeHiveRawValue,
        id: { value: queenChangeHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QueenChangeHiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateQueenChange: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
      dateFinished: currentTime,
    };
  }

  private convertQueenChangeHiveRawValueToQueenChangeHive(
    rawQueenChangeHive: QueenChangeHiveFormRawValue | NewQueenChangeHiveFormRawValue,
  ): IQueenChangeHive | NewQueenChangeHive {
    return {
      ...rawQueenChangeHive,
      dateQueenChange: dayjs(rawQueenChangeHive.dateQueenChange, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawQueenChangeHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawQueenChangeHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawQueenChangeHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawQueenChangeHive.dateDeleted, DATE_TIME_FORMAT),
      dateFinished: dayjs(rawQueenChangeHive.dateFinished, DATE_TIME_FORMAT),
    };
  }

  private convertQueenChangeHiveToQueenChangeHiveRawValue(
    queenChangeHive: IQueenChangeHive | (Partial<NewQueenChangeHive> & QueenChangeHiveFormDefaults),
  ): QueenChangeHiveFormRawValue | PartialWithRequiredKeyOf<NewQueenChangeHiveFormRawValue> {
    return {
      ...queenChangeHive,
      dateQueenChange: queenChangeHive.dateQueenChange ? queenChangeHive.dateQueenChange.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: queenChangeHive.dateCreated ? queenChangeHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: queenChangeHive.dateModified ? queenChangeHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: queenChangeHive.dateSynched ? queenChangeHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: queenChangeHive.dateDeleted ? queenChangeHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
      dateFinished: queenChangeHive.dateFinished ? queenChangeHive.dateFinished.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
