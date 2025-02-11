import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeedingHive, NewFeedingHive } from '../feeding-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedingHive for edit and NewFeedingHiveFormGroupInput for create.
 */
type FeedingHiveFormGroupInput = IFeedingHive | PartialWithRequiredKeyOf<NewFeedingHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeedingHive | NewFeedingHive> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type FeedingHiveFormRawValue = FormValueOf<IFeedingHive>;

type NewFeedingHiveFormRawValue = FormValueOf<NewFeedingHive>;

type FeedingHiveFormDefaults = Pick<NewFeedingHive, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type FeedingHiveFormGroupContent = {
  id: FormControl<FeedingHiveFormRawValue['id'] | NewFeedingHive['id']>;
  foodAmount: FormControl<FeedingHiveFormRawValue['foodAmount']>;
  externalId: FormControl<FeedingHiveFormRawValue['externalId']>;
  uuid: FormControl<FeedingHiveFormRawValue['uuid']>;
  dateCreated: FormControl<FeedingHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<FeedingHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<FeedingHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<FeedingHiveFormRawValue['dateDeleted']>;
  user: FormControl<FeedingHiveFormRawValue['user']>;
};

export type FeedingHiveFormGroup = FormGroup<FeedingHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedingHiveFormService {
  createFeedingHiveFormGroup(feedingHive: FeedingHiveFormGroupInput = { id: null }): FeedingHiveFormGroup {
    const feedingHiveRawValue = this.convertFeedingHiveToFeedingHiveRawValue({
      ...this.getFormDefaults(),
      ...feedingHive,
    });
    return new FormGroup<FeedingHiveFormGroupContent>({
      id: new FormControl(
        { value: feedingHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      foodAmount: new FormControl(feedingHiveRawValue.foodAmount),
      externalId: new FormControl(feedingHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(feedingHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(feedingHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(feedingHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(feedingHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(feedingHiveRawValue.dateDeleted),
      user: new FormControl(feedingHiveRawValue.user),
    });
  }

  getFeedingHive(form: FeedingHiveFormGroup): IFeedingHive | NewFeedingHive {
    return this.convertFeedingHiveRawValueToFeedingHive(form.getRawValue() as FeedingHiveFormRawValue | NewFeedingHiveFormRawValue);
  }

  resetForm(form: FeedingHiveFormGroup, feedingHive: FeedingHiveFormGroupInput): void {
    const feedingHiveRawValue = this.convertFeedingHiveToFeedingHiveRawValue({ ...this.getFormDefaults(), ...feedingHive });
    form.reset(
      {
        ...feedingHiveRawValue,
        id: { value: feedingHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedingHiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertFeedingHiveRawValueToFeedingHive(
    rawFeedingHive: FeedingHiveFormRawValue | NewFeedingHiveFormRawValue,
  ): IFeedingHive | NewFeedingHive {
    return {
      ...rawFeedingHive,
      dateCreated: dayjs(rawFeedingHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawFeedingHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawFeedingHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawFeedingHive.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertFeedingHiveToFeedingHiveRawValue(
    feedingHive: IFeedingHive | (Partial<NewFeedingHive> & FeedingHiveFormDefaults),
  ): FeedingHiveFormRawValue | PartialWithRequiredKeyOf<NewFeedingHiveFormRawValue> {
    return {
      ...feedingHive,
      dateCreated: feedingHive.dateCreated ? feedingHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: feedingHive.dateModified ? feedingHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: feedingHive.dateSynched ? feedingHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: feedingHive.dateDeleted ? feedingHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
