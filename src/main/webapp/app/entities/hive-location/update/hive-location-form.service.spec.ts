import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../hive-location.test-samples';

import { HiveLocationFormService } from './hive-location-form.service';

describe('HiveLocation Form Service', () => {
  let service: HiveLocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HiveLocationFormService);
  });

  describe('Service methods', () => {
    describe('createHiveLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHiveLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
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

      it('passing IHiveLocation should create a new form with FormGroup', () => {
        const formGroup = service.createHiveLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
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

    describe('getHiveLocation', () => {
      it('should return NewHiveLocation for default HiveLocation initial value', () => {
        const formGroup = service.createHiveLocationFormGroup(sampleWithNewData);

        const hiveLocation = service.getHiveLocation(formGroup) as any;

        expect(hiveLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewHiveLocation for empty HiveLocation initial value', () => {
        const formGroup = service.createHiveLocationFormGroup();

        const hiveLocation = service.getHiveLocation(formGroup) as any;

        expect(hiveLocation).toMatchObject({});
      });

      it('should return IHiveLocation', () => {
        const formGroup = service.createHiveLocationFormGroup(sampleWithRequiredData);

        const hiveLocation = service.getHiveLocation(formGroup) as any;

        expect(hiveLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHiveLocation should not enable id FormControl', () => {
        const formGroup = service.createHiveLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHiveLocation should disable id FormControl', () => {
        const formGroup = service.createHiveLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
