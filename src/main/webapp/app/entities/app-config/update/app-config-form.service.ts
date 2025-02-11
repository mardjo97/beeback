import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppConfig, NewAppConfig } from '../app-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppConfig for edit and NewAppConfigFormGroupInput for create.
 */
type AppConfigFormGroupInput = IAppConfig | PartialWithRequiredKeyOf<NewAppConfig>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAppConfig | NewAppConfig> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type AppConfigFormRawValue = FormValueOf<IAppConfig>;

type NewAppConfigFormRawValue = FormValueOf<NewAppConfig>;

type AppConfigFormDefaults = Pick<NewAppConfig, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type AppConfigFormGroupContent = {
  id: FormControl<AppConfigFormRawValue['id'] | NewAppConfig['id']>;
  key: FormControl<AppConfigFormRawValue['key']>;
  type: FormControl<AppConfigFormRawValue['type']>;
  value: FormControl<AppConfigFormRawValue['value']>;
  externalId: FormControl<AppConfigFormRawValue['externalId']>;
  uuid: FormControl<AppConfigFormRawValue['uuid']>;
  dateCreated: FormControl<AppConfigFormRawValue['dateCreated']>;
  dateModified: FormControl<AppConfigFormRawValue['dateModified']>;
  dateSynched: FormControl<AppConfigFormRawValue['dateSynched']>;
  dateDeleted: FormControl<AppConfigFormRawValue['dateDeleted']>;
  user: FormControl<AppConfigFormRawValue['user']>;
};

export type AppConfigFormGroup = FormGroup<AppConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppConfigFormService {
  createAppConfigFormGroup(appConfig: AppConfigFormGroupInput = { id: null }): AppConfigFormGroup {
    const appConfigRawValue = this.convertAppConfigToAppConfigRawValue({
      ...this.getFormDefaults(),
      ...appConfig,
    });
    return new FormGroup<AppConfigFormGroupContent>({
      id: new FormControl(
        { value: appConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      key: new FormControl(appConfigRawValue.key),
      type: new FormControl(appConfigRawValue.type),
      value: new FormControl(appConfigRawValue.value),
      externalId: new FormControl(appConfigRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(appConfigRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(appConfigRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(appConfigRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(appConfigRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(appConfigRawValue.dateDeleted),
      user: new FormControl(appConfigRawValue.user),
    });
  }

  getAppConfig(form: AppConfigFormGroup): IAppConfig | NewAppConfig {
    return this.convertAppConfigRawValueToAppConfig(form.getRawValue() as AppConfigFormRawValue | NewAppConfigFormRawValue);
  }

  resetForm(form: AppConfigFormGroup, appConfig: AppConfigFormGroupInput): void {
    const appConfigRawValue = this.convertAppConfigToAppConfigRawValue({ ...this.getFormDefaults(), ...appConfig });
    form.reset(
      {
        ...appConfigRawValue,
        id: { value: appConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppConfigFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertAppConfigRawValueToAppConfig(rawAppConfig: AppConfigFormRawValue | NewAppConfigFormRawValue): IAppConfig | NewAppConfig {
    return {
      ...rawAppConfig,
      dateCreated: dayjs(rawAppConfig.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawAppConfig.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawAppConfig.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawAppConfig.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertAppConfigToAppConfigRawValue(
    appConfig: IAppConfig | (Partial<NewAppConfig> & AppConfigFormDefaults),
  ): AppConfigFormRawValue | PartialWithRequiredKeyOf<NewAppConfigFormRawValue> {
    return {
      ...appConfig,
      dateCreated: appConfig.dateCreated ? appConfig.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: appConfig.dateModified ? appConfig.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: appConfig.dateSynched ? appConfig.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: appConfig.dateDeleted ? appConfig.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
