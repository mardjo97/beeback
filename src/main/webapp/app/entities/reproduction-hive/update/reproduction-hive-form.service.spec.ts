import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reproduction-hive.test-samples';

import { ReproductionHiveFormService } from './reproduction-hive-form.service';

describe('ReproductionHive Form Service', () => {
  let service: ReproductionHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReproductionHiveFormService);
  });

  describe('Service methods', () => {
    describe('createReproductionHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReproductionHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            dateFinished: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });

      it('passing IReproductionHive should create a new form with FormGroup', () => {
        const formGroup = service.createReproductionHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            dateFinished: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });
    });

    describe('getReproductionHive', () => {
      it('should return NewReproductionHive for default ReproductionHive initial value', () => {
        const formGroup = service.createReproductionHiveFormGroup(sampleWithNewData);

        const reproductionHive = service.getReproductionHive(formGroup) as any;

        expect(reproductionHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewReproductionHive for empty ReproductionHive initial value', () => {
        const formGroup = service.createReproductionHiveFormGroup();

        const reproductionHive = service.getReproductionHive(formGroup) as any;

        expect(reproductionHive).toMatchObject({});
      });

      it('should return IReproductionHive', () => {
        const formGroup = service.createReproductionHiveFormGroup(sampleWithRequiredData);

        const reproductionHive = service.getReproductionHive(formGroup) as any;

        expect(reproductionHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReproductionHive should not enable id FormControl', () => {
        const formGroup = service.createReproductionHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReproductionHive should disable id FormControl', () => {
        const formGroup = service.createReproductionHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
