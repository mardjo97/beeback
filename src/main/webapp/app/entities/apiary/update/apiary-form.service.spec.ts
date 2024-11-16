import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../apiary.test-samples';

import { ApiaryFormService } from './apiary-form.service';

describe('Apiary Form Service', () => {
  let service: ApiaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiaryFormService);
  });

  describe('Service methods', () => {
    describe('createApiaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApiaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            idNumber: expect.any(Object),
            color: expect.any(Object),
            location: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            orderNumber: expect.any(Object),
            hiveCount: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IApiary should create a new form with FormGroup', () => {
        const formGroup = service.createApiaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            idNumber: expect.any(Object),
            color: expect.any(Object),
            location: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            orderNumber: expect.any(Object),
            hiveCount: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getApiary', () => {
      it('should return NewApiary for default Apiary initial value', () => {
        const formGroup = service.createApiaryFormGroup(sampleWithNewData);

        const apiary = service.getApiary(formGroup) as any;

        expect(apiary).toMatchObject(sampleWithNewData);
      });

      it('should return NewApiary for empty Apiary initial value', () => {
        const formGroup = service.createApiaryFormGroup();

        const apiary = service.getApiary(formGroup) as any;

        expect(apiary).toMatchObject({});
      });

      it('should return IApiary', () => {
        const formGroup = service.createApiaryFormGroup(sampleWithRequiredData);

        const apiary = service.getApiary(formGroup) as any;

        expect(apiary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApiary should not enable id FormControl', () => {
        const formGroup = service.createApiaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApiary should disable id FormControl', () => {
        const formGroup = service.createApiaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
