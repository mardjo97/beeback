import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../harvest-type.test-samples';

import { HarvestTypeFormService } from './harvest-type-form.service';

describe('HarvestType Form Service', () => {
  let service: HarvestTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HarvestTypeFormService);
  });

  describe('Service methods', () => {
    describe('createHarvestTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHarvestTypeFormGroup();

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

      it('passing IHarvestType should create a new form with FormGroup', () => {
        const formGroup = service.createHarvestTypeFormGroup(sampleWithRequiredData);

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

    describe('getHarvestType', () => {
      it('should return NewHarvestType for default HarvestType initial value', () => {
        const formGroup = service.createHarvestTypeFormGroup(sampleWithNewData);

        const harvestType = service.getHarvestType(formGroup) as any;

        expect(harvestType).toMatchObject(sampleWithNewData);
      });

      it('should return NewHarvestType for empty HarvestType initial value', () => {
        const formGroup = service.createHarvestTypeFormGroup();

        const harvestType = service.getHarvestType(formGroup) as any;

        expect(harvestType).toMatchObject({});
      });

      it('should return IHarvestType', () => {
        const formGroup = service.createHarvestTypeFormGroup(sampleWithRequiredData);

        const harvestType = service.getHarvestType(formGroup) as any;

        expect(harvestType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHarvestType should not enable id FormControl', () => {
        const formGroup = service.createHarvestTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHarvestType should disable id FormControl', () => {
        const formGroup = service.createHarvestTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
