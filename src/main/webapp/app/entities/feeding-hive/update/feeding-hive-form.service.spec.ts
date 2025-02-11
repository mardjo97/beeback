import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../feeding-hive.test-samples';

import { FeedingHiveFormService } from './feeding-hive-form.service';

describe('FeedingHive Form Service', () => {
  let service: FeedingHiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedingHiveFormService);
  });

  describe('Service methods', () => {
    describe('createFeedingHiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedingHiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            foodAmount: expect.any(Object),
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

      it('passing IFeedingHive should create a new form with FormGroup', () => {
        const formGroup = service.createFeedingHiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            foodAmount: expect.any(Object),
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

    describe('getFeedingHive', () => {
      it('should return NewFeedingHive for default FeedingHive initial value', () => {
        const formGroup = service.createFeedingHiveFormGroup(sampleWithNewData);

        const feedingHive = service.getFeedingHive(formGroup) as any;

        expect(feedingHive).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedingHive for empty FeedingHive initial value', () => {
        const formGroup = service.createFeedingHiveFormGroup();

        const feedingHive = service.getFeedingHive(formGroup) as any;

        expect(feedingHive).toMatchObject({});
      });

      it('should return IFeedingHive', () => {
        const formGroup = service.createFeedingHiveFormGroup(sampleWithRequiredData);

        const feedingHive = service.getFeedingHive(formGroup) as any;

        expect(feedingHive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedingHive should not enable id FormControl', () => {
        const formGroup = service.createFeedingHiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedingHive should disable id FormControl', () => {
        const formGroup = service.createFeedingHiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
