import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGoodHarvestHive, NewGoodHarvestHive } from '../good-harvest-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGoodHarvestHive for edit and NewGoodHarvestHiveFormGroupInput for create.
 */
type GoodHarvestHiveFormGroupInput = IGoodHarvestHive | PartialWithRequiredKeyOf<NewGoodHarvestHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGoodHarvestHive | NewGoodHarvestHive> = Omit<
  T,
  'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type GoodHarvestHiveFormRawValue = FormValueOf<IGoodHarvestHive>;

type NewGoodHarvestHiveFormRawValue = FormValueOf<NewGoodHarvestHive>;

type GoodHarvestHiveFormDefaults = Pick<NewGoodHarvestHive, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type GoodHarvestHiveFormGroupContent = {
  id: FormControl<GoodHarvestHiveFormRawValue['id'] | NewGoodHarvestHive['id']>;
  amount: FormControl<GoodHarvestHiveFormRawValue['amount']>;
  externalId: FormControl<GoodHarvestHiveFormRawValue['externalId']>;
  uuid: FormControl<GoodHarvestHiveFormRawValue['uuid']>;
  dateCreated: FormControl<GoodHarvestHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<GoodHarvestHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<GoodHarvestHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<GoodHarvestHiveFormRawValue['dateDeleted']>;
  user: FormControl<GoodHarvestHiveFormRawValue['user']>;
  hive: FormControl<GoodHarvestHiveFormRawValue['hive']>;
  harvestType: FormControl<GoodHarvestHiveFormRawValue['harvestType']>;
};

export type GoodHarvestHiveFormGroup = FormGroup<GoodHarvestHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GoodHarvestHiveFormService {
  createGoodHarvestHiveFormGroup(goodHarvestHive: GoodHarvestHiveFormGroupInput = { id: null }): GoodHarvestHiveFormGroup {
    const goodHarvestHiveRawValue = this.convertGoodHarvestHiveToGoodHarvestHiveRawValue({
      ...this.getFormDefaults(),
      ...goodHarvestHive,
    });
    return new FormGroup<GoodHarvestHiveFormGroupContent>({
      id: new FormControl(
        { value: goodHarvestHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      amount: new FormControl(goodHarvestHiveRawValue.amount),
      externalId: new FormControl(goodHarvestHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(goodHarvestHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(goodHarvestHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(goodHarvestHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(goodHarvestHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(goodHarvestHiveRawValue.dateDeleted),
      user: new FormControl(goodHarvestHiveRawValue.user),
      hive: new FormControl(goodHarvestHiveRawValue.hive),
      harvestType: new FormControl(goodHarvestHiveRawValue.harvestType),
    });
  }

  getGoodHarvestHive(form: GoodHarvestHiveFormGroup): IGoodHarvestHive | NewGoodHarvestHive {
    return this.convertGoodHarvestHiveRawValueToGoodHarvestHive(
      form.getRawValue() as GoodHarvestHiveFormRawValue | NewGoodHarvestHiveFormRawValue,
    );
  }

  resetForm(form: GoodHarvestHiveFormGroup, goodHarvestHive: GoodHarvestHiveFormGroupInput): void {
    const goodHarvestHiveRawValue = this.convertGoodHarvestHiveToGoodHarvestHiveRawValue({ ...this.getFormDefaults(), ...goodHarvestHive });
    form.reset(
      {
        ...goodHarvestHiveRawValue,
        id: { value: goodHarvestHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GoodHarvestHiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertGoodHarvestHiveRawValueToGoodHarvestHive(
    rawGoodHarvestHive: GoodHarvestHiveFormRawValue | NewGoodHarvestHiveFormRawValue,
  ): IGoodHarvestHive | NewGoodHarvestHive {
    return {
      ...rawGoodHarvestHive,
      dateCreated: dayjs(rawGoodHarvestHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawGoodHarvestHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawGoodHarvestHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawGoodHarvestHive.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertGoodHarvestHiveToGoodHarvestHiveRawValue(
    goodHarvestHive: IGoodHarvestHive | (Partial<NewGoodHarvestHive> & GoodHarvestHiveFormDefaults),
  ): GoodHarvestHiveFormRawValue | PartialWithRequiredKeyOf<NewGoodHarvestHiveFormRawValue> {
    return {
      ...goodHarvestHive,
      dateCreated: goodHarvestHive.dateCreated ? goodHarvestHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: goodHarvestHive.dateModified ? goodHarvestHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: goodHarvestHive.dateSynched ? goodHarvestHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: goodHarvestHive.dateDeleted ? goodHarvestHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
