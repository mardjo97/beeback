import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHarvest, NewHarvest } from '../harvest.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHarvest for edit and NewHarvestFormGroupInput for create.
 */
type HarvestFormGroupInput = IHarvest | PartialWithRequiredKeyOf<NewHarvest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHarvest | NewHarvest> = Omit<
  T,
  'dateCollected' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  dateCollected?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type HarvestFormRawValue = FormValueOf<IHarvest>;

type NewHarvestFormRawValue = FormValueOf<NewHarvest>;

type HarvestFormDefaults = Pick<NewHarvest, 'id' | 'dateCollected' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type HarvestFormGroupContent = {
  id: FormControl<HarvestFormRawValue['id'] | NewHarvest['id']>;
  hiveFrames: FormControl<HarvestFormRawValue['hiveFrames']>;
  amount: FormControl<HarvestFormRawValue['amount']>;
  dateCollected: FormControl<HarvestFormRawValue['dateCollected']>;
  group: FormControl<HarvestFormRawValue['group']>;
  groupRecordId: FormControl<HarvestFormRawValue['groupRecordId']>;
  externalId: FormControl<HarvestFormRawValue['externalId']>;
  uuid: FormControl<HarvestFormRawValue['uuid']>;
  dateCreated: FormControl<HarvestFormRawValue['dateCreated']>;
  dateModified: FormControl<HarvestFormRawValue['dateModified']>;
  dateSynched: FormControl<HarvestFormRawValue['dateSynched']>;
  dateDeleted: FormControl<HarvestFormRawValue['dateDeleted']>;
  user: FormControl<HarvestFormRawValue['user']>;
  hive: FormControl<HarvestFormRawValue['hive']>;
  harvestType: FormControl<HarvestFormRawValue['harvestType']>;
};

export type HarvestFormGroup = FormGroup<HarvestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HarvestFormService {
  createHarvestFormGroup(harvest: HarvestFormGroupInput = { id: null }): HarvestFormGroup {
    const harvestRawValue = this.convertHarvestToHarvestRawValue({
      ...this.getFormDefaults(),
      ...harvest,
    });
    return new FormGroup<HarvestFormGroupContent>({
      id: new FormControl(
        { value: harvestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      hiveFrames: new FormControl(harvestRawValue.hiveFrames),
      amount: new FormControl(harvestRawValue.amount),
      dateCollected: new FormControl(harvestRawValue.dateCollected),
      group: new FormControl(harvestRawValue.group),
      groupRecordId: new FormControl(harvestRawValue.groupRecordId),
      externalId: new FormControl(harvestRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(harvestRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(harvestRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(harvestRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(harvestRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(harvestRawValue.dateDeleted),
      user: new FormControl(harvestRawValue.user),
      hive: new FormControl(harvestRawValue.hive),
      harvestType: new FormControl(harvestRawValue.harvestType),
    });
  }

  getHarvest(form: HarvestFormGroup): IHarvest | NewHarvest {
    return this.convertHarvestRawValueToHarvest(form.getRawValue() as HarvestFormRawValue | NewHarvestFormRawValue);
  }

  resetForm(form: HarvestFormGroup, harvest: HarvestFormGroupInput): void {
    const harvestRawValue = this.convertHarvestToHarvestRawValue({ ...this.getFormDefaults(), ...harvest });
    form.reset(
      {
        ...harvestRawValue,
        id: { value: harvestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HarvestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCollected: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertHarvestRawValueToHarvest(rawHarvest: HarvestFormRawValue | NewHarvestFormRawValue): IHarvest | NewHarvest {
    return {
      ...rawHarvest,
      dateCollected: dayjs(rawHarvest.dateCollected, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawHarvest.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawHarvest.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawHarvest.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawHarvest.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertHarvestToHarvestRawValue(
    harvest: IHarvest | (Partial<NewHarvest> & HarvestFormDefaults),
  ): HarvestFormRawValue | PartialWithRequiredKeyOf<NewHarvestFormRawValue> {
    return {
      ...harvest,
      dateCollected: harvest.dateCollected ? harvest.dateCollected.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: harvest.dateCreated ? harvest.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: harvest.dateModified ? harvest.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: harvest.dateSynched ? harvest.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: harvest.dateDeleted ? harvest.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
