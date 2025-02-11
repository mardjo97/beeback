import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../good-harvest-hive.test-samples';

import { GoodHarvestHiveFormService } from './good-harvest-hive-form.service';

describe('GoodHarvestHive Form Service', () => {
  let service: GoodHarvestHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GoodHarvestHiveFormService);
  });

  describe('Service methods', () => {
    describe('createGoodHarvestHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            harvestType: expect.any(Object),
          }),
        );
      });

      it('passing IGoodHarvestHive should create a new form with FormGroup', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            harvestType: expect.any(Object),
          }),
        );
      });
    });

    describe('getGoodHarvestHive', () => {
      it('should return NewGoodHarvestHive for default GoodHarvestHive initial value', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup(sampleWithNewData);

        const goodHarvestHive = service.getGoodHarvestHive(formGroup) as any;

        expect(goodHarvestHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewGoodHarvestHive for empty GoodHarvestHive initial value', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup();

        const goodHarvestHive = service.getGoodHarvestHive(formGroup) as any;

        expect(goodHarvestHive).toMatchObject({});
      });

      it('should return IGoodHarvestHive', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup(sampleWithRequiredData);

        const goodHarvestHive = service.getGoodHarvestHive(formGroup) as any;

        expect(goodHarvestHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGoodHarvestHive should not enable id FormControl', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGoodHarvestHive should disable id FormControl', () => {
        const formGroup = service.createGoodHarvestHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
