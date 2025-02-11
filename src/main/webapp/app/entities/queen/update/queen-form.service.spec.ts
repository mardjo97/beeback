import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../queen.test-samples';

import { QueenFormService } from './queen-form.service';

describe('Queen Form Service', () => {
  let service: QueenFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QueenFormService);
  });

  describe('Service methods', () => {
    describe('createQueenFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQueenFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origin: expect.any(Object),
            year: expect.any(Object),
            isMarked: expect.any(Object),
            active: expect.any(Object),
            activeFromDate: expect.any(Object),
            activeToDate: expect.any(Object),
            queenChangeDate: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });

      it('passing IQueen should create a new form with FormGroup', () => {
        const formGroup = service.createQueenFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origin: expect.any(Object),
            year: expect.any(Object),
            isMarked: expect.any(Object),
            active: expect.any(Object),
            activeFromDate: expect.any(Object),
            activeToDate: expect.any(Object),
            queenChangeDate: expect.any(Object),
            externalId: expect.any(Object),
            uuid: expect.any(Object),
            dateCreated: expect.any(Object),
            dateModified: expect.any(Object),
            dateSynched: expect.any(Object),
            dateDeleted: expect.any(Object),
            user: expect.any(Object),
            hive: expect.any(Object),
          }),
        );
      });
    });

    describe('getQueen', () => {
      it('should return NewQueen for default Queen initial value', () => {
        const formGroup = service.createQueenFormGroup(sampleWithNewData);

        const queen = service.getQueen(formGroup) as any;

        expect(queen).toMatchObject(sampleWithNewData);
      });

      it('should return NewQueen for empty Queen initial value', () => {
        const formGroup = service.createQueenFormGroup();

        const queen = service.getQueen(formGroup) as any;

        expect(queen).toMatchObject({});
      });

      it('should return IQueen', () => {
        const formGroup = service.createQueenFormGroup(sampleWithRequiredData);

        const queen = service.getQueen(formGroup) as any;

        expect(queen).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQueen should not enable id FormControl', () => {
        const formGroup = service.createQueenFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQueen should disable id FormControl', () => {
        const formGroup = service.createQueenFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
