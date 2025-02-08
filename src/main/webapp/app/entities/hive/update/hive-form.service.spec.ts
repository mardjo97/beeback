import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../hive.test-samples';

import { HiveFormService } from './hive-form.service';

describe('Hive Form Service', () => {
  let service: HiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HiveFormService);
  });

  describe('Service methods', () => {
    describe('createHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            barcode: expect.any(Object),
            orderNumber: expect.any(Object),
            description: expect.any(Object),
            examinationDate: expect.any(Object),
            archivedDate: expect.any(Object),
            archivedReason: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hiveType: expect.any(Object),
            apiary: expect.any(Object),
          }),
        );
      });

      it('passing IHive should create a new form with FormGroup', () => {
        const formGroup = service.createHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            barcode: expect.any(Object),
            orderNumber: expect.any(Object),
            description: expect.any(Object),
            examinationDate: expect.any(Object),
            archivedDate: expect.any(Object),
            archivedReason: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hiveType: expect.any(Object),
            apiary: expect.any(Object),
          }),
        );
      });
    });

    describe('getHive', () => {
      it('should return NewHive for default Hive initial value', () => {
        const formGroup = service.createHiveFormGroup(sampleWithNewData);

        const hive = service.getHive(formGroup) as any;

        expect(hive).toMatchObject(sampleWithNewData);
      });

      it('should return NewHive for empty Hive initial value', () => {
        const formGroup = service.createHiveFormGroup();

        const hive = service.getHive(formGroup) as any;

        expect(hive).toMatchObject({});
      });

      it('should return IHive', () => {
        const formGroup = service.createHiveFormGroup(sampleWithRequiredData);

        const hive = service.getHive(formGroup) as any;

        expect(hive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHive should not enable id FormControl', () => {
        const formGroup = service.createHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHive should disable id FormControl', () => {
        const formGroup = service.createHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
