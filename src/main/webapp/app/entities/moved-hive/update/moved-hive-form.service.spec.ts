import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../moved-hive.test-samples';

import { MovedHiveFormService } from './moved-hive-form.service';

describe('MovedHive Form Service', () => {
  let service: MovedHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovedHiveFormService);
  });

  describe('Service methods', () => {
    describe('createMovedHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMovedHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            location: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
            harvestType: expect.any(Object),
          }),
        );
      });

      it('passing IMovedHive should create a new form with FormGroup', () => {
        const formGroup = service.createMovedHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            location: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
            harvestType: expect.any(Object),
          }),
        );
      });
    });

    describe('getMovedHive', () => {
      it('should return NewMovedHive for default MovedHive initial value', () => {
        const formGroup = service.createMovedHiveFormGroup(sampleWithNewData);

        const movedHive = service.getMovedHive(formGroup) as any;

        expect(movedHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewMovedHive for empty MovedHive initial value', () => {
        const formGroup = service.createMovedHiveFormGroup();

        const movedHive = service.getMovedHive(formGroup) as any;

        expect(movedHive).toMatchObject({});
      });

      it('should return IMovedHive', () => {
        const formGroup = service.createMovedHiveFormGroup(sampleWithRequiredData);

        const movedHive = service.getMovedHive(formGroup) as any;

        expect(movedHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMovedHive should not enable id FormControl', () => {
        const formGroup = service.createMovedHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMovedHive should disable id FormControl', () => {
        const formGroup = service.createMovedHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
