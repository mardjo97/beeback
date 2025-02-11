import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../queen-change-hive.test-samples';

import { QueenChangeHiveFormService } from './queen-change-hive-form.service';

describe('QueenChangeHive Form Service', () => {
  let service: QueenChangeHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QueenChangeHiveFormService);
  });

  describe('Service methods', () => {
    describe('createQueenChangeHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQueenChangeHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateQueenChange: expect.any(Object),
            reminderId: expect.any(Object),
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

      it('passing IQueenChangeHive should create a new form with FormGroup', () => {
        const formGroup = service.createQueenChangeHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateQueenChange: expect.any(Object),
            reminderId: expect.any(Object),
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

    describe('getQueenChangeHive', () => {
      it('should return NewQueenChangeHive for default QueenChangeHive initial value', () => {
        const formGroup = service.createQueenChangeHiveFormGroup(sampleWithNewData);

        const queenChangeHive = service.getQueenChangeHive(formGroup) as any;

        expect(queenChangeHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewQueenChangeHive for empty QueenChangeHive initial value', () => {
        const formGroup = service.createQueenChangeHiveFormGroup();

        const queenChangeHive = service.getQueenChangeHive(formGroup) as any;

        expect(queenChangeHive).toMatchObject({});
      });

      it('should return IQueenChangeHive', () => {
        const formGroup = service.createQueenChangeHiveFormGroup(sampleWithRequiredData);

        const queenChangeHive = service.getQueenChangeHive(formGroup) as any;

        expect(queenChangeHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQueenChangeHive should not enable id FormControl', () => {
        const formGroup = service.createQueenChangeHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQueenChangeHive should disable id FormControl', () => {
        const formGroup = service.createQueenChangeHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
