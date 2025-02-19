import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReproductionHive, NewReproductionHive } from '../reproduction-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReproductionHive for edit and NewReproductionHiveFormGroupInput for create.
 */
type ReproductionHiveFormGroupInput = IReproductionHive | PartialWithRequiredKeyOf<NewReproductionHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReproductionHive | NewReproductionHive> = Omit<
  T,
  'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
  dateFinished?: string | null;
};

type ReproductionHiveFormRawValue = FormValueOf<IReproductionHive>;

type NewReproductionHiveFormRawValue = FormValueOf<NewReproductionHive>;

type ReproductionHiveFormDefaults = Pick<
  NewReproductionHive,
  'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
>;

type ReproductionHiveFormGroupContent = {
  id: FormControl<ReproductionHiveFormRawValue['id'] | NewReproductionHive['id']>;
  note: FormControl<ReproductionHiveFormRawValue['note']>;
  externalId: FormControl<ReproductionHiveFormRawValue['externalId']>;
  uuid: FormControl<ReproductionHiveFormRawValue['uuid']>;
  dateCreated: FormControl<ReproductionHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<ReproductionHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<ReproductionHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<ReproductionHiveFormRawValue['dateDeleted']>;
  dateFinished: FormControl<ReproductionHiveFormRawValue['dateFinished']>;
  user: FormControl<ReproductionHiveFormRawValue['user']>;
  hive: FormControl<ReproductionHiveFormRawValue['hive']>;
};

export type ReproductionHiveFormGroup = FormGroup<ReproductionHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReproductionHiveFormService {
  createReproductionHiveFormGroup(reproductionHive: ReproductionHiveFormGroupInput = { id: null }): ReproductionHiveFormGroup {
    const reproductionHiveRawValue = this.convertReproductionHiveToReproductionHiveRawValue({
      ...this.getFormDefaults(),
      ...reproductionHive,
    });
    return new FormGroup<ReproductionHiveFormGroupContent>({
      id: new FormControl(
        { value: reproductionHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(reproductionHiveRawValue.note),
      externalId: new FormControl(reproductionHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(reproductionHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(reproductionHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(reproductionHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(reproductionHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(reproductionHiveRawValue.dateDeleted),
      dateFinished: new FormControl(reproductionHiveRawValue.dateFinished),
      user: new FormControl(reproductionHiveRawValue.user),
      hive: new FormControl(reproductionHiveRawValue.hive),
    });
  }

  getReproductionHive(form: ReproductionHiveFormGroup): IReproductionHive | NewReproductionHive {
    return this.convertReproductionHiveRawValueToReproductionHive(
      form.getRawValue() as ReproductionHiveFormRawValue | NewReproductionHiveFormRawValue,
    );
  }

  resetForm(form: ReproductionHiveFormGroup, reproductionHive: ReproductionHiveFormGroupInput): void {
    const reproductionHiveRawValue = this.convertReproductionHiveToReproductionHiveRawValue({
      ...this.getFormDefaults(),
      ...reproductionHive,
    });
    form.reset(
      {
        ...reproductionHiveRawValue,
        id: { value: reproductionHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReproductionHiveFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
      dateFinished: currentTime,
    };
  }

  private convertReproductionHiveRawValueToReproductionHive(
    rawReproductionHive: ReproductionHiveFormRawValue | NewReproductionHiveFormRawValue,
  ): IReproductionHive | NewReproductionHive {
    return {
      ...rawReproductionHive,
      dateCreated: dayjs(rawReproductionHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawReproductionHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawReproductionHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawReproductionHive.dateDeleted, DATE_TIME_FORMAT),
      dateFinished: dayjs(rawReproductionHive.dateFinished, DATE_TIME_FORMAT),
    };
  }

  private convertReproductionHiveToReproductionHiveRawValue(
    reproductionHive: IReproductionHive | (Partial<NewReproductionHive> & ReproductionHiveFormDefaults),
  ): ReproductionHiveFormRawValue | PartialWithRequiredKeyOf<NewReproductionHiveFormRawValue> {
    return {
      ...reproductionHive,
      dateCreated: reproductionHive.dateCreated ? reproductionHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: reproductionHive.dateModified ? reproductionHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: reproductionHive.dateSynched ? reproductionHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: reproductionHive.dateDeleted ? reproductionHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
      dateFinished: reproductionHive.dateFinished ? reproductionHive.dateFinished.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
