import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHiveType, NewHiveType } from '../hive-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHiveType for edit and NewHiveTypeFormGroupInput for create.
 */
type HiveTypeFormGroupInput = IHiveType | PartialWithRequiredKeyOf<NewHiveType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHiveType | NewHiveType> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type HiveTypeFormRawValue = FormValueOf<IHiveType>;

type NewHiveTypeFormRawValue = FormValueOf<NewHiveType>;

type HiveTypeFormDefaults = Pick<NewHiveType, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type HiveTypeFormGroupContent = {
  id: FormControl<HiveTypeFormRawValue['id'] | NewHiveType['id']>;
  Name: FormControl<HiveTypeFormRawValue['Name']>;
  externalId: FormControl<HiveTypeFormRawValue['externalId']>;
  uuid: FormControl<HiveTypeFormRawValue['uuid']>;
  dateCreated: FormControl<HiveTypeFormRawValue['dateCreated']>;
  dateModified: FormControl<HiveTypeFormRawValue['dateModified']>;
  dateSynched: FormControl<HiveTypeFormRawValue['dateSynched']>;
  dateDeleted: FormControl<HiveTypeFormRawValue['dateDeleted']>;
  user: FormControl<HiveTypeFormRawValue['user']>;
};

export type HiveTypeFormGroup = FormGroup<HiveTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HiveTypeFormService {
  createHiveTypeFormGroup(hiveType: HiveTypeFormGroupInput = { id: null }): HiveTypeFormGroup {
    const hiveTypeRawValue = this.convertHiveTypeToHiveTypeRawValue({
      ...this.getFormDefaults(),
      ...hiveType,
    });
    return new FormGroup<HiveTypeFormGroupContent>({
      id: new FormControl(
        { value: hiveTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      Name: new FormControl(hiveTypeRawValue.Name, {
        validators: [Validators.required],
      }),
      externalId: new FormControl(hiveTypeRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(hiveTypeRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(hiveTypeRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(hiveTypeRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(hiveTypeRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(hiveTypeRawValue.dateDeleted),
      user: new FormControl(hiveTypeRawValue.user),
    });
  }

  getHiveType(form: HiveTypeFormGroup): IHiveType | NewHiveType {
    return this.convertHiveTypeRawValueToHiveType(form.getRawValue() as HiveTypeFormRawValue | NewHiveTypeFormRawValue);
  }

  resetForm(form: HiveTypeFormGroup, hiveType: HiveTypeFormGroupInput): void {
    const hiveTypeRawValue = this.convertHiveTypeToHiveTypeRawValue({ ...this.getFormDefaults(), ...hiveType });
    form.reset(
      {
        ...hiveTypeRawValue,
        id: { value: hiveTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HiveTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertHiveTypeRawValueToHiveType(rawHiveType: HiveTypeFormRawValue | NewHiveTypeFormRawValue): IHiveType | NewHiveType {
    return {
      ...rawHiveType,
      dateCreated: dayjs(rawHiveType.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawHiveType.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawHiveType.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawHiveType.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertHiveTypeToHiveTypeRawValue(
    hiveType: IHiveType | (Partial<NewHiveType> & HiveTypeFormDefaults),
  ): HiveTypeFormRawValue | PartialWithRequiredKeyOf<NewHiveTypeFormRawValue> {
    return {
      ...hiveType,
      dateCreated: hiveType.dateCreated ? hiveType.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: hiveType.dateModified ? hiveType.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: hiveType.dateSynched ? hiveType.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: hiveType.dateDeleted ? hiveType.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
