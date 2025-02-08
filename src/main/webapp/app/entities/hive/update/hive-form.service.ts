import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHive, NewHive } from '../hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHive for edit and NewHiveFormGroupInput for create.
 */
type HiveFormGroupInput = IHive | PartialWithRequiredKeyOf<NewHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHive | NewHive> = Omit<
  T,
  'examinationDate' | 'archivedDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  examinationDate?: string | null;
  archivedDate?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type HiveFormRawValue = FormValueOf<IHive>;

type NewHiveFormRawValue = FormValueOf<NewHive>;

type HiveFormDefaults = Pick<
  NewHive,
  'id' | 'examinationDate' | 'archivedDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
>;

type HiveFormGroupContent = {
  id: FormControl<HiveFormRawValue['id'] | NewHive['id']>;
  barcode: FormControl<HiveFormRawValue['barcode']>;
  orderNumber: FormControl<HiveFormRawValue['orderNumber']>;
  description: FormControl<HiveFormRawValue['description']>;
  examinationDate: FormControl<HiveFormRawValue['examinationDate']>;
  archivedDate: FormControl<HiveFormRawValue['archivedDate']>;
  archivedReason: FormControl<HiveFormRawValue['archivedReason']>;
  externalId: FormControl<HiveFormRawValue['externalId']>;
  uuid: FormControl<HiveFormRawValue['uuid']>;
  dateCreated: FormControl<HiveFormRawValue['dateCreated']>;
  dateModified: FormControl<HiveFormRawValue['dateModified']>;
  dateSynched: FormControl<HiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<HiveFormRawValue['dateDeleted']>;
  user: FormControl<HiveFormRawValue['user']>;
  hiveType: FormControl<HiveFormRawValue['hiveType']>;
  apiary: FormControl<HiveFormRawValue['apiary']>;
};

export type HiveFormGroup = FormGroup<HiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HiveFormService {
  createHiveFormGroup(hive: HiveFormGroupInput = { id: null }): HiveFormGroup {
    const hiveRawValue = this.convertHiveToHiveRawValue({
      ...this.getFormDefaults(),
      ...hive,
    });
    return new FormGroup<HiveFormGroupContent>({
      id: new FormControl(
        { value: hiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      barcode: new FormControl(hiveRawValue.barcode, {
        validators: [Validators.required],
      }),
      orderNumber: new FormControl(hiveRawValue.orderNumber),
      description: new FormControl(hiveRawValue.description),
      examinationDate: new FormControl(hiveRawValue.examinationDate),
      archivedDate: new FormControl(hiveRawValue.archivedDate),
      archivedReason: new FormControl(hiveRawValue.archivedReason),
      externalId: new FormControl(hiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(hiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(hiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(hiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(hiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(hiveRawValue.dateDeleted),
      user: new FormControl(hiveRawValue.user),
      hiveType: new FormControl(hiveRawValue.hiveType),
      apiary: new FormControl(hiveRawValue.apiary),
    });
  }

  getHive(form: HiveFormGroup): IHive | NewHive {
    return this.convertHiveRawValueToHive(form.getRawValue() as HiveFormRawValue | NewHiveFormRawValue);
  }

  resetForm(form: HiveFormGroup, hive: HiveFormGroupInput): void {
    const hiveRawValue = this.convertHiveToHiveRawValue({ ...this.getFormDefaults(), ...hive });
    form.reset(
      {
        ...hiveRawValue,
        id: { value: hiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      examinationDate: currentTime,
      archivedDate: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertHiveRawValueToHive(rawHive: HiveFormRawValue | NewHiveFormRawValue): IHive | NewHive {
    return {
      ...rawHive,
      examinationDate: dayjs(rawHive.examinationDate, DATE_TIME_FORMAT),
      archivedDate: dayjs(rawHive.archivedDate, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawHive.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertHiveToHiveRawValue(
    hive: IHive | (Partial<NewHive> & HiveFormDefaults),
  ): HiveFormRawValue | PartialWithRequiredKeyOf<NewHiveFormRawValue> {
    return {
      ...hive,
      examinationDate: hive.examinationDate ? hive.examinationDate.format(DATE_TIME_FORMAT) : undefined,
      archivedDate: hive.archivedDate ? hive.archivedDate.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: hive.dateCreated ? hive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: hive.dateModified ? hive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: hive.dateSynched ? hive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: hive.dateDeleted ? hive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
