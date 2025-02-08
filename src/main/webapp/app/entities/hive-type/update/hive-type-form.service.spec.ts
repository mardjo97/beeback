import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../hive-type.test-samples';

import { HiveTypeFormService } from './hive-type-form.service';

describe('HiveType Form Service', () => {
  let service: HiveTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HiveTypeFormService);
  });

  describe('Service methods', () => {
    describe('createHiveTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHiveTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            Name: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IHiveType should create a new form with FormGroup', () => {
        const formGroup = service.createHiveTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            Name: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getHiveType', () => {
      it('should return NewHiveType for default HiveType initial value', () => {
        const formGroup = service.createHiveTypeFormGroup(sampleWithNewData);

        const hiveType = service.getHiveType(formGroup) as any;

        expect(hiveType).toMatchObject(sampleWithNewData);
      });

      it('should return NewHiveType for empty HiveType initial value', () => {
        const formGroup = service.createHiveTypeFormGroup();

        const hiveType = service.getHiveType(formGroup) as any;

        expect(hiveType).toMatchObject({});
      });

      it('should return IHiveType', () => {
        const formGroup = service.createHiveTypeFormGroup(sampleWithRequiredData);

        const hiveType = service.getHiveType(formGroup) as any;

        expect(hiveType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHiveType should not enable id FormControl', () => {
        const formGroup = service.createHiveTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHiveType should disable id FormControl', () => {
        const formGroup = service.createHiveTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
