import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMovedHive, NewMovedHive } from '../moved-hive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMovedHive for edit and NewMovedHiveFormGroupInput for create.
 */
type MovedHiveFormGroupInput = IMovedHive | PartialWithRequiredKeyOf<NewMovedHive>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMovedHive | NewMovedHive> = Omit<
  T,
  'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
  dateFinished?: string | null;
};

type MovedHiveFormRawValue = FormValueOf<IMovedHive>;

type NewMovedHiveFormRawValue = FormValueOf<NewMovedHive>;

type MovedHiveFormDefaults = Pick<NewMovedHive, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'>;

type MovedHiveFormGroupContent = {
  id: FormControl<MovedHiveFormRawValue['id'] | NewMovedHive['id']>;
  location: FormControl<MovedHiveFormRawValue['location']>;
  externalId: FormControl<MovedHiveFormRawValue['externalId']>;
  uuid: FormControl<MovedHiveFormRawValue['uuid']>;
  dateCreated: FormControl<MovedHiveFormRawValue['dateCreated']>;
  dateModified: FormControl<MovedHiveFormRawValue['dateModified']>;
  dateSynched: FormControl<MovedHiveFormRawValue['dateSynched']>;
  dateDeleted: FormControl<MovedHiveFormRawValue['dateDeleted']>;
  dateFinished: FormControl<MovedHiveFormRawValue['dateFinished']>;
  user: FormControl<MovedHiveFormRawValue['user']>;
  hive: FormControl<MovedHiveFormRawValue['hive']>;
  harvestType: FormControl<MovedHiveFormRawValue['harvestType']>;
};

export type MovedHiveFormGroup = FormGroup<MovedHiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MovedHiveFormService {
  createMovedHiveFormGroup(movedHive: MovedHiveFormGroupInput = { id: null }): MovedHiveFormGroup {
    const movedHiveRawValue = this.convertMovedHiveToMovedHiveRawValue({
      ...this.getFormDefaults(),
      ...movedHive,
    });
    return new FormGroup<MovedHiveFormGroupContent>({
      id: new FormControl(
        { value: movedHiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      location: new FormControl(movedHiveRawValue.location),
      externalId: new FormControl(movedHiveRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(movedHiveRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(movedHiveRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(movedHiveRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(movedHiveRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(movedHiveRawValue.dateDeleted),
      dateFinished: new FormControl(movedHiveRawValue.dateFinished),
      user: new FormControl(movedHiveRawValue.user),
      hive: new FormControl(movedHiveRawValue.hive),
      harvestType: new FormControl(movedHiveRawValue.harvestType),
    });
  }

  getMovedHive(form: MovedHiveFormGroup): IMovedHive | NewMovedHive {
    return this.convertMovedHiveRawValueToMovedHive(form.getRawValue() as MovedHiveFormRawValue | NewMovedHiveFormRawValue);
  }

  resetForm(form: MovedHiveFormGroup, movedHive: MovedHiveFormGroupInput): void {
    const movedHiveRawValue = this.convertMovedHiveToMovedHiveRawValue({ ...this.getFormDefaults(), ...movedHive });
    form.reset(
      {
        ...movedHiveRawValue,
        id: { value: movedHiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MovedHiveFormDefaults {
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

  private convertMovedHiveRawValueToMovedHive(rawMovedHive: MovedHiveFormRawValue | NewMovedHiveFormRawValue): IMovedHive | NewMovedHive {
    return {
      ...rawMovedHive,
      dateCreated: dayjs(rawMovedHive.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawMovedHive.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawMovedHive.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawMovedHive.dateDeleted, DATE_TIME_FORMAT),
      dateFinished: dayjs(rawMovedHive.dateFinished, DATE_TIME_FORMAT),
    };
  }

  private convertMovedHiveToMovedHiveRawValue(
    movedHive: IMovedHive | (Partial<NewMovedHive> & MovedHiveFormDefaults),
  ): MovedHiveFormRawValue | PartialWithRequiredKeyOf<NewMovedHiveFormRawValue> {
    return {
      ...movedHive,
      dateCreated: movedHive.dateCreated ? movedHive.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: movedHive.dateModified ? movedHive.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: movedHive.dateSynched ? movedHive.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: movedHive.dateDeleted ? movedHive.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
      dateFinished: movedHive.dateFinished ? movedHive.dateFinished.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
