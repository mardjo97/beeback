import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApiary, NewApiary } from '../apiary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApiary for edit and NewApiaryFormGroupInput for create.
 */
type ApiaryFormGroupInput = IApiary | PartialWithRequiredKeyOf<NewApiary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApiary | NewApiary> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
};

type ApiaryFormRawValue = FormValueOf<IApiary>;

type NewApiaryFormRawValue = FormValueOf<NewApiary>;

type ApiaryFormDefaults = Pick<NewApiary, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched'>;

type ApiaryFormGroupContent = {
  id: FormControl<ApiaryFormRawValue['id'] | NewApiary['id']>;
  name: FormControl<ApiaryFormRawValue['name']>;
  idNumber: FormControl<ApiaryFormRawValue['idNumber']>;
  color: FormControl<ApiaryFormRawValue['color']>;
  location: FormControl<ApiaryFormRawValue['location']>;
  latitude: FormControl<ApiaryFormRawValue['latitude']>;
  longitude: FormControl<ApiaryFormRawValue['longitude']>;
  orderNumber: FormControl<ApiaryFormRawValue['orderNumber']>;
  hiveCount: FormControl<ApiaryFormRawValue['hiveCount']>;
  externalId: FormControl<ApiaryFormRawValue['externalId']>;
  uuid: FormControl<ApiaryFormRawValue['uuid']>;
  dateCreated: FormControl<ApiaryFormRawValue['dateCreated']>;
  dateModified: FormControl<ApiaryFormRawValue['dateModified']>;
  dateSynched: FormControl<ApiaryFormRawValue['dateSynched']>;
  user: FormControl<ApiaryFormRawValue['user']>;
};

export type ApiaryFormGroup = FormGroup<ApiaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApiaryFormService {
  createApiaryFormGroup(apiary: ApiaryFormGroupInput = { id: null }): ApiaryFormGroup {
    const apiaryRawValue = this.convertApiaryToApiaryRawValue({
      ...this.getFormDefaults(),
      ...apiary,
    });
    return new FormGroup<ApiaryFormGroupContent>({
      id: new FormControl(
        { value: apiaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(apiaryRawValue.name),
      idNumber: new FormControl(apiaryRawValue.idNumber),
      color: new FormControl(apiaryRawValue.color),
      location: new FormControl(apiaryRawValue.location),
      latitude: new FormControl(apiaryRawValue.latitude),
      longitude: new FormControl(apiaryRawValue.longitude),
      orderNumber: new FormControl(apiaryRawValue.orderNumber),
      hiveCount: new FormControl(apiaryRawValue.hiveCount),
      externalId: new FormControl(apiaryRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(apiaryRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(apiaryRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(apiaryRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(apiaryRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      user: new FormControl(apiaryRawValue.user),
    });
  }

  getApiary(form: ApiaryFormGroup): IApiary | NewApiary {
    return this.convertApiaryRawValueToApiary(form.getRawValue() as ApiaryFormRawValue | NewApiaryFormRawValue);
  }

  resetForm(form: ApiaryFormGroup, apiary: ApiaryFormGroupInput): void {
    const apiaryRawValue = this.convertApiaryToApiaryRawValue({ ...this.getFormDefaults(), ...apiary });
    form.reset(
      {
        ...apiaryRawValue,
        id: { value: apiaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApiaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
    };
  }

  private convertApiaryRawValueToApiary(rawApiary: ApiaryFormRawValue | NewApiaryFormRawValue): IApiary | NewApiary {
    return {
      ...rawApiary,
      dateCreated: dayjs(rawApiary.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawApiary.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawApiary.dateSynched, DATE_TIME_FORMAT),
    };
  }

  private convertApiaryToApiaryRawValue(
    apiary: IApiary | (Partial<NewApiary> & ApiaryFormDefaults),
  ): ApiaryFormRawValue | PartialWithRequiredKeyOf<NewApiaryFormRawValue> {
    return {
      ...apiary,
      dateCreated: apiary.dateCreated ? apiary.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: apiary.dateModified ? apiary.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: apiary.dateSynched ? apiary.dateSynched.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
