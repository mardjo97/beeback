import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHiveLocation, NewHiveLocation } from '../hive-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHiveLocation for edit and NewHiveLocationFormGroupInput for create.
 */
type HiveLocationFormGroupInput = IHiveLocation | PartialWithRequiredKeyOf<NewHiveLocation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHiveLocation | NewHiveLocation> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type HiveLocationFormRawValue = FormValueOf<IHiveLocation>;

type NewHiveLocationFormRawValue = FormValueOf<NewHiveLocation>;

type HiveLocationFormDefaults = Pick<NewHiveLocation, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type HiveLocationFormGroupContent = {
  id: FormControl<HiveLocationFormRawValue['id'] | NewHiveLocation['id']>;
  name: FormControl<HiveLocationFormRawValue['name']>;
  externalId: FormControl<HiveLocationFormRawValue['externalId']>;
  uuid: FormControl<HiveLocationFormRawValue['uuid']>;
  dateCreated: FormControl<HiveLocationFormRawValue['dateCreated']>;
  dateModified: FormControl<HiveLocationFormRawValue['dateModified']>;
  dateSynched: FormControl<HiveLocationFormRawValue['dateSynched']>;
  dateDeleted: FormControl<HiveLocationFormRawValue['dateDeleted']>;
  user: FormControl<HiveLocationFormRawValue['user']>;
};

export type HiveLocationFormGroup = FormGroup<HiveLocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HiveLocationFormService {
  createHiveLocationFormGroup(hiveLocation: HiveLocationFormGroupInput = { id: null }): HiveLocationFormGroup {
    const hiveLocationRawValue = this.convertHiveLocationToHiveLocationRawValue({
      ...this.getFormDefaults(),
      ...hiveLocation,
    });
    return new FormGroup<HiveLocationFormGroupContent>({
      id: new FormControl(
        { value: hiveLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(hiveLocationRawValue.name),
      externalId: new FormControl(hiveLocationRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(hiveLocationRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(hiveLocationRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(hiveLocationRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(hiveLocationRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(hiveLocationRawValue.dateDeleted),
      user: new FormControl(hiveLocationRawValue.user),
    });
  }

  getHiveLocation(form: HiveLocationFormGroup): IHiveLocation | NewHiveLocation {
    return this.convertHiveLocationRawValueToHiveLocation(form.getRawValue() as HiveLocationFormRawValue | NewHiveLocationFormRawValue);
  }

  resetForm(form: HiveLocationFormGroup, hiveLocation: HiveLocationFormGroupInput): void {
    const hiveLocationRawValue = this.convertHiveLocationToHiveLocationRawValue({ ...this.getFormDefaults(), ...hiveLocation });
    form.reset(
      {
        ...hiveLocationRawValue,
        id: { value: hiveLocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HiveLocationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertHiveLocationRawValueToHiveLocation(
    rawHiveLocation: HiveLocationFormRawValue | NewHiveLocationFormRawValue,
  ): IHiveLocation | NewHiveLocation {
    return {
      ...rawHiveLocation,
      dateCreated: dayjs(rawHiveLocation.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawHiveLocation.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawHiveLocation.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawHiveLocation.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertHiveLocationToHiveLocationRawValue(
    hiveLocation: IHiveLocation | (Partial<NewHiveLocation> & HiveLocationFormDefaults),
  ): HiveLocationFormRawValue | PartialWithRequiredKeyOf<NewHiveLocationFormRawValue> {
    return {
      ...hiveLocation,
      dateCreated: hiveLocation.dateCreated ? hiveLocation.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: hiveLocation.dateModified ? hiveLocation.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: hiveLocation.dateSynched ? hiveLocation.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: hiveLocation.dateDeleted ? hiveLocation.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
