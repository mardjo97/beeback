import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../harvest.test-samples';

import { HarvestFormService } from './harvest-form.service';

describe('Harvest Form Service', () => {
  let service: HarvestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HarvestFormService);
  });

  describe('Service methods', () => {
    describe('createHarvestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHarvestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hiveFrames: expect.any(Object),
            amount: expect.any(Object),
            dateCollected: expect.any(Object),
            group: expect.any(Object),
            groupRecordId: expect.any(Object),
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

      it('passing IHarvest should create a new form with FormGroup', () => {
        const formGroup = service.createHarvestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hiveFrames: expect.any(Object),
            amount: expect.any(Object),
            dateCollected: expect.any(Object),
            group: expect.any(Object),
            groupRecordId: expect.any(Object),
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

    describe('getHarvest', () => {
      it('should return NewHarvest for default Harvest initial value', () => {
        const formGroup = service.createHarvestFormGroup(sampleWithNewData);

        const harvest = service.getHarvest(formGroup) as any;

        expect(harvest).toMatchObject(sampleWithNewData);
      });

      it('should return NewHarvest for empty Harvest initial value', () => {
        const formGroup = service.createHarvestFormGroup();

        const harvest = service.getHarvest(formGroup) as any;

        expect(harvest).toMatchObject({});
      });

      it('should return IHarvest', () => {
        const formGroup = service.createHarvestFormGroup(sampleWithRequiredData);

        const harvest = service.getHarvest(formGroup) as any;

        expect(harvest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHarvest should not enable id FormControl', () => {
        const formGroup = service.createHarvestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHarvest should disable id FormControl', () => {
        const formGroup = service.createHarvestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
