import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExaminationHive, NewExaminationHive } from '../examination-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExaminationHive for edit and NewExaminationHiveFormGroupInput for create.
 */
type ExaminationHiveFormGroupInput = IExaminationHive | PartialWithRequiredKeyOf<NewExaminationHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExaminationHive | NewExaminationHive> = Omit<
  T,
  'dateExamination' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  dateExamination?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type ExaminationHiveFormRawValue = FormValueOf<IExaminationHive>;

type NewExaminationHiveFormRawValue = FormValueOf<NewExaminationHive>;

type ExaminationHiveFormDefaults = Pick<
  NewExaminationHive,
  'id' | 'dateExamination' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
>;

type ExaminationHiveFormGroupContent = {
  id: FormControl<ExaminationHiveFormRawValue['id'] | NewExaminationHive['id']>;
  note: FormControl<ExaminationHiveFormRawValue['note']>;
  dateExamination: FormControl<ExaminationHiveFormRawValue['dateExamination']>;
  reminderId: FormControl<ExaminationHiveFormRawValue['reminderId']>;
  externalId: FormControl<ExaminationHiveFormRawValue['externalId']>;
  uuid: FormControl<ExaminationHiveFormRawValue['uuid']>;
  dateCreated: FormControl<ExaminationHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<ExaminationHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<ExaminationHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<ExaminationHiveFormRawValue['dateDeleted']>;
  user: FormControl<ExaminationHiveFormRawValue['user']>;
};

export type ExaminationHiveFormGroup = FormGroup<ExaminationHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExaminationHiveFormService {
  createExaminationHiveFormGroup(examinationHive: ExaminationHiveFormGroupInput = { id: null }): ExaminationHiveFormGroup {
    const examinationHiveRawValue = this.convertExaminationHiveToExaminationHiveRawValue({
      ...this.getFormDefaults(),
      ...examinationHive,
    });
    return new FormGroup<ExaminationHiveFormGroupContent>({
      id: new FormControl(
        { value: examinationHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(examinationHiveRawValue.note),
      dateExamination: new FormControl(examinationHiveRawValue.dateExamination),
      reminderId: new FormControl(examinationHiveRawValue.reminderId),
      externalId: new FormControl(examinationHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(examinationHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(examinationHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(examinationHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(examinationHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(examinationHiveRawValue.dateDeleted),
      user: new FormControl(examinationHiveRawValue.user),
    });
  }

  getExaminationHive(form: ExaminationHiveFormGroup): IExaminationHive | NewExaminationHive {
    return this.convertExaminationHiveRawValueToExaminationHive(
      form.getRawValue() as ExaminationHiveFormRawValue | NewExaminationHiveFormRawValue,
    );
  }

  resetForm(form: ExaminationHiveFormGroup, examinationHive: ExaminationHiveFormGroupInput): void {
    const examinationHiveRawValue = this.convertExaminationHiveToExaminationHiveRawValue({ ...this.getFormDefaults(), ...examinationHive });
    form.reset(
      {
        ...examinationHiveRawValue,
        id: { value: examinationHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExaminationHiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateExamination: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertExaminationHiveRawValueToExaminationHive(
    rawExaminationHive: ExaminationHiveFormRawValue | NewExaminationHiveFormRawValue,
  ): IExaminationHive | NewExaminationHive {
    return {
      ...rawExaminationHive,
      dateExamination: dayjs(rawExaminationHive.dateExamination, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawExaminationHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawExaminationHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawExaminationHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawExaminationHive.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertExaminationHiveToExaminationHiveRawValue(
    examinationHive: IExaminationHive | (Partial<NewExaminationHive> & ExaminationHiveFormDefaults),
  ): ExaminationHiveFormRawValue | PartialWithRequiredKeyOf<NewExaminationHiveFormRawValue> {
    return {
      ...examinationHive,
      dateExamination: examinationHive.dateExamination ? examinationHive.dateExamination.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: examinationHive.dateCreated ? examinationHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: examinationHive.dateModified ? examinationHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: examinationHive.dateSynched ? examinationHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: examinationHive.dateDeleted ? examinationHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
