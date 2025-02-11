import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHarvestType, NewHarvestType } from '../harvest-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHarvestType for edit and NewHarvestTypeFormGroupInput for create.
 */
type HarvestTypeFormGroupInput = IHarvestType | PartialWithRequiredKeyOf<NewHarvestType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHarvestType | NewHarvestType> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type HarvestTypeFormRawValue = FormValueOf<IHarvestType>;

type NewHarvestTypeFormRawValue = FormValueOf<NewHarvestType>;

type HarvestTypeFormDefaults = Pick<NewHarvestType, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type HarvestTypeFormGroupContent = {
  id: FormControl<HarvestTypeFormRawValue['id'] | NewHarvestType['id']>;
  name: FormControl<HarvestTypeFormRawValue['name']>;
  externalId: FormControl<HarvestTypeFormRawValue['externalId']>;
  uuid: FormControl<HarvestTypeFormRawValue['uuid']>;
  dateCreated: FormControl<HarvestTypeFormRawValue['dateCreated']>;
  dateModified: FormControl<HarvestTypeFormRawValue['dateModified']>;
  dateSynched: FormControl<HarvestTypeFormRawValue['dateSynched']>;
  dateDeleted: FormControl<HarvestTypeFormRawValue['dateDeleted']>;
  user: FormControl<HarvestTypeFormRawValue['user']>;
};

export type HarvestTypeFormGroup = FormGroup<HarvestTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HarvestTypeFormService {
  createHarvestTypeFormGroup(harvestType: HarvestTypeFormGroupInput = { id: null }): HarvestTypeFormGroup {
    const harvestTypeRawValue = this.convertHarvestTypeToHarvestTypeRawValue({
      ...this.getFormDefaults(),
      ...harvestType,
    });
    return new FormGroup<HarvestTypeFormGroupContent>({
      id: new FormControl(
        { value: harvestTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(harvestTypeRawValue.name),
      externalId: new FormControl(harvestTypeRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(harvestTypeRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(harvestTypeRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(harvestTypeRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(harvestTypeRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(harvestTypeRawValue.dateDeleted),
      user: new FormControl(harvestTypeRawValue.user),
    });
  }

  getHarvestType(form: HarvestTypeFormGroup): IHarvestType | NewHarvestType {
    return this.convertHarvestTypeRawValueToHarvestType(form.getRawValue() as HarvestTypeFormRawValue | NewHarvestTypeFormRawValue);
  }

  resetForm(form: HarvestTypeFormGroup, harvestType: HarvestTypeFormGroupInput): void {
    const harvestTypeRawValue = this.convertHarvestTypeToHarvestTypeRawValue({ ...this.getFormDefaults(), ...harvestType });
    form.reset(
      {
        ...harvestTypeRawValue,
        id: { value: harvestTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HarvestTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertHarvestTypeRawValueToHarvestType(
    rawHarvestType: HarvestTypeFormRawValue | NewHarvestTypeFormRawValue,
  ): IHarvestType | NewHarvestType {
    return {
      ...rawHarvestType,
      dateCreated: dayjs(rawHarvestType.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawHarvestType.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawHarvestType.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawHarvestType.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertHarvestTypeToHarvestTypeRawValue(
    harvestType: IHarvestType | (Partial<NewHarvestType> & HarvestTypeFormDefaults),
  ): HarvestTypeFormRawValue | PartialWithRequiredKeyOf<NewHarvestTypeFormRawValue> {
    return {
      ...harvestType,
      dateCreated: harvestType.dateCreated ? harvestType.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: harvestType.dateModified ? harvestType.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: harvestType.dateSynched ? harvestType.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: harvestType.dateDeleted ? harvestType.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
