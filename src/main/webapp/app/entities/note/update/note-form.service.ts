import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INote, NewNote } from '../note.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INote for edit and NewNoteFormGroupInput for create.
 */
type NoteFormGroupInput = INote | PartialWithRequiredKeyOf<NewNote>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INote | NewNote> = Omit<
  T,
  'dateHidden' | 'reminderDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  dateHidden?: string | null;
  reminderDate?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

type NoteFormRawValue = FormValueOf<INote>;

type NewNoteFormRawValue = FormValueOf<NewNote>;

type NoteFormDefaults = Pick<
  NewNote,
  'id' | 'hasReminder' | 'dateHidden' | 'reminderDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
>;

type NoteFormGroupContent = {
  id: FormControl<NoteFormRawValue['id'] | NewNote['id']>;
  hasReminder: FormControl<NoteFormRawValue['hasReminder']>;
  title: FormControl<NoteFormRawValue['title']>;
  content: FormControl<NoteFormRawValue['content']>;
  group: FormControl<NoteFormRawValue['group']>;
  groupRecordId: FormControl<NoteFormRawValue['groupRecordId']>;
  dateHidden: FormControl<NoteFormRawValue['dateHidden']>;
  reminderDate: FormControl<NoteFormRawValue['reminderDate']>;
  reminderId: FormControl<NoteFormRawValue['reminderId']>;
  externalId: FormControl<NoteFormRawValue['externalId']>;
  uuid: FormControl<NoteFormRawValue['uuid']>;
  dateCreated: FormControl<NoteFormRawValue['dateCreated']>;
  dateModified: FormControl<NoteFormRawValue['dateModified']>;
  dateSynched: FormControl<NoteFormRawValue['dateSynched']>;
  dateDeleted: FormControl<NoteFormRawValue['dateDeleted']>;
  user: FormControl<NoteFormRawValue['user']>;
  hive: FormControl<NoteFormRawValue['hive']>;
};

export type NoteFormGroup = FormGroup<NoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NoteFormService {
  createNoteFormGroup(note: NoteFormGroupInput = { id: null }): NoteFormGroup {
    const noteRawValue = this.convertNoteToNoteRawValue({
      ...this.getFormDefaults(),
      ...note,
    });
    return new FormGroup<NoteFormGroupContent>({
      id: new FormControl(
        { value: noteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      hasReminder: new FormControl(noteRawValue.hasReminder),
      title: new FormControl(noteRawValue.title),
      content: new FormControl(noteRawValue.content),
      group: new FormControl(noteRawValue.group),
      groupRecordId: new FormControl(noteRawValue.groupRecordId),
      dateHidden: new FormControl(noteRawValue.dateHidden),
      reminderDate: new FormControl(noteRawValue.reminderDate),
      reminderId: new FormControl(noteRawValue.reminderId),
      externalId: new FormControl(noteRawValue.externalId, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(noteRawValue.uuid, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(noteRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(noteRawValue.dateModified, {
        validators: [Validators.required],
      }),
      dateSynched: new FormControl(noteRawValue.dateSynched, {
        validators: [Validators.required],
      }),
      dateDeleted: new FormControl(noteRawValue.dateDeleted),
      user: new FormControl(noteRawValue.user),
      hive: new FormControl(noteRawValue.hive),
    });
  }

  getNote(form: NoteFormGroup): INote | NewNote {
    return this.convertNoteRawValueToNote(form.getRawValue() as NoteFormRawValue | NewNoteFormRawValue);
  }

  resetForm(form: NoteFormGroup, note: NoteFormGroupInput): void {
    const noteRawValue = this.convertNoteToNoteRawValue({ ...this.getFormDefaults(), ...note });
    form.reset(
      {
        ...noteRawValue,
        id: { value: noteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NoteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hasReminder: false,
      dateHidden: currentTime,
      reminderDate: currentTime,
      dateCreated: currentTime,
      dateModified: currentTime,
      dateSynched: currentTime,
      dateDeleted: currentTime,
    };
  }

  private convertNoteRawValueToNote(rawNote: NoteFormRawValue | NewNoteFormRawValue): INote | NewNote {
    return {
      ...rawNote,
      dateHidden: dayjs(rawNote.dateHidden, DATE_TIME_FORMAT),
      reminderDate: dayjs(rawNote.reminderDate, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawNote.dateCreated, DATE_TIME_FORMAT),
      dateModified: dayjs(rawNote.dateModified, DATE_TIME_FORMAT),
      dateSynched: dayjs(rawNote.dateSynched, DATE_TIME_FORMAT),
      dateDeleted: dayjs(rawNote.dateDeleted, DATE_TIME_FORMAT),
    };
  }

  private convertNoteToNoteRawValue(
    note: INote | (Partial<NewNote> & NoteFormDefaults),
  ): NoteFormRawValue | PartialWithRequiredKeyOf<NewNoteFormRawValue> {
    return {
      ...note,
      dateHidden: note.dateHidden ? note.dateHidden.format(DATE_TIME_FORMAT) : undefined,
      reminderDate: note.reminderDate ? note.reminderDate.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: note.dateCreated ? note.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateModified: note.dateModified ? note.dateModified.format(DATE_TIME_FORMAT) : undefined,
      dateSynched: note.dateSynched ? note.dateSynched.format(DATE_TIME_FORMAT) : undefined,
      dateDeleted: note.dateDeleted ? note.dateDeleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
