import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../examination-hive.test-samples';

import { ExaminationHiveFormService } from './examination-hive-form.service';

describe('ExaminationHive Form Service', () => {
  let service: ExaminationHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExaminationHiveFormService);
  });

  describe('Service methods', () => {
    describe('createExaminationHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExaminationHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            dateExamination: expect.any(Object),
            reminderId: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });

      it('passing IExaminationHive should create a new form with FormGroup', () => {
        const formGroup = service.createExaminationHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            dateExamination: expect.any(Object),
            reminderId: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });
    });

    describe('getExaminationHive', () => {
      it('should return NewExaminationHive for default ExaminationHive initial value', () => {
        const formGroup = service.createExaminationHiveFormGroup(sampleWithNewData);

        const examinationHive = service.getExaminationHive(formGroup) as any;

        expect(examinationHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewExaminationHive for empty ExaminationHive initial value', () => {
        const formGroup = service.createExaminationHiveFormGroup();

        const examinationHive = service.getExaminationHive(formGroup) as any;

        expect(examinationHive).toMatchObject({});
      });

      it('should return IExaminationHive', () => {
        const formGroup = service.createExaminationHiveFormGroup(sampleWithRequiredData);

        const examinationHive = service.getExaminationHive(formGroup) as any;

        expect(examinationHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExaminationHive should not enable id FormControl', () => {
        const formGroup = service.createExaminationHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExaminationHive should disable id FormControl', () => {
        const formGroup = service.createExaminationHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
