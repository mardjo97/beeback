import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGroup, NewGroup } from '../group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGroup for edit and NewGroupFormGroupInput for create.
 */
type GroupFormGroupInput = IGroup | PartialWithRequiredKeyOf<NewGroup>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGroup | NewGroup> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type GroupFormRawValue = FormValueOf<IGroup>;

type NewGroupFormRawValue = FormValueOf<NewGroup>;

type GroupFormDefaults = Pick<NewGroup, 'id' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'>;

type GroupFormGroupContent = {
  id: FormControl<GroupFormRawValue['id'] | NewGroup['id']>;
  name: FormControl<GroupFormRawValue['name']>;
  enumValueName: FormControl<GroupFormRawValue['enumValueName']>;
  color: FormControl<GroupFormRawValue['color']>;
  hiveCount: FormControl<GroupFormRawValue['hiveCount']>;
  hiveCountFinished: FormControl<GroupFormRawValue['hiveCountFinished']>;
  additionalInfo: FormControl<GroupFormRawValue['additionalInfo']>;
  orderNumber: FormControl<GroupFormRawValue['orderNumber']>;
  externalId: FormControl<GroupFormRawValue['externalId']>;
  uuid: FormControl<GroupFormRawValue['uuid']>;
  dateCreated: FormControl<GroupFormRawValue['dateCreated']>;
  dateModified: FormControl<GroupFormRawValue['dateModified']>;
  dateSynched: FormControl<GroupFormRawValue['dateSynched']>;
  dateDeleted: FormControl<GroupFormRawValue['dateDeleted']>;
  user: FormControl<GroupFormRawValue['user']>;
};

export type GroupFormGroup = FormGroup<GroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GroupFormService {
  createGroupFormGroup(group: GroupFormGroupInput = { id: null }): GroupFormGroup {
    const groupRawValue = this.convertGroupToGroupRawValue({
      ...this.getFormDefaults(),
      ...group,
    });
    return new FormGroup<GroupFormGroupContent>({
      id: new FormControl(
        { value: groupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(groupRawValue.name),
      enumValueName: new FormControl(groupRawValue.enumValueName),
      color: new FormControl(groupRawValue.color),
      hiveCount: new FormControl(groupRawValue.hiveCount),
      hiveCountFinished: new FormControl(groupRawValue.hiveCountFinished),
      additionalInfo: new FormControl(groupRawValue.additionalInfo),
      orderNumber: new FormControl(groupRawValue.orderNumber),
      externalId: new FormControl(groupRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(groupRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(groupRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(groupRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(groupRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(groupRawValue.dateDeleted),
      user: new FormControl(groupRawValue.user),
    });
  }

  getGroup(form: GroupFormGroup): IGroup | NewGroup {
    return this.convertGroupRawValueToGroup(form.getRawValue() as GroupFormRawValue | NewGroupFormRawValue);
  }

  resetForm(form: GroupFormGroup, group: GroupFormGroupInput): void {
    const groupRawValue = this.convertGroupToGroupRawValue({ ...this.getFormDefaults(), ...group });
    form.reset(
      {
        ...groupRawValue,
        id: { value: groupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GroupFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertGroupRawValueToGroup(rawGroup: GroupFormRawValue | NewGroupFormRawValue): IGroup | NewGroup {
    return {
      ...rawGroup,
      dateCreated: dayjs(rawGroup.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawGroup.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawGroup.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawGroup.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertGroupToGroupRawValue(
    group: IGroup | (Partial<NewGroup> & GroupFormDefaults),
  ): GroupFormRawValue | PartialWithRequiredKeyOf<NewGroupFormRawValue> {
    return {
      ...group,
      dateCreated: group.dateCreated ? group.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: group.dateModified ? group.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: group.dateSynched ? group.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: group.dateDeleted ? group.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
