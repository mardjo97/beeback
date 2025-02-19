import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeedingHive } from '../feeding-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../feeding-hive.test-samples';

import { FeedingHiveService, RestFeedingHive } from './feeding-hive.service';

const requireRestSample: RestFeedingHive = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
  dateFinished: sampleWithRequiredData.dateFinished?.toJSON(),
};

describe('FeedingHive Service', () => {
  let service: FeedingHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedingHive | IFeedingHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeedingHiveService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FeedingHive', () => {
      const feedingHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedingHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedingHive', () => {
      const feedingHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedingHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedingHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedingHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedingHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedingHiveToCollectionIfMissing', () => {
      it('should add a FeedingHive to an empty array', () => {
        const feedingHive: IFeedingHive = sampleWithRequiredData;
        expectedResult = service.addFeedingHiveToCollectionIfMissing([], feedingHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedingHive);
      });

      it('should not add a FeedingHive to an array that contains it', () => {
        const feedingHive: IFeedingHive = sampleWithRequiredData;
        const feedingHiveCollection: IFeedingHive[] = [
          {
            ...feedingHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedingHiveToCollectionIfMissing(feedingHiveCollection, feedingHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedingHive to an array that doesn't contain it", () => {
        const feedingHive: IFeedingHive = sampleWithRequiredData;
        const feedingHiveCollection: IFeedingHive[] = [sampleWithPartialData];
        expectedResult = service.addFeedingHiveToCollectionIfMissing(feedingHiveCollection, feedingHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedingHive);
      });

      it('should add only unique FeedingHive to an array', () => {
        const feedingHiveArray: IFeedingHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedingHiveCollection: IFeedingHive[] = [sampleWithRequiredData];
        expectedResult = service.addFeedingHiveToCollectionIfMissing(feedingHiveCollection, ...feedingHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedingHive: IFeedingHive = sampleWithRequiredData;
        const feedingHive2: IFeedingHive = sampleWithPartialData;
        expectedResult = service.addFeedingHiveToCollectionIfMissing([], feedingHive, feedingHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedingHive);
        expect(expectedResult).toContain(feedingHive2);
      });

      it('should accept null and undefined values', () => {
        const feedingHive: IFeedingHive = sampleWithRequiredData;
        expectedResult = service.addFeedingHiveToCollectionIfMissing([], null, feedingHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedingHive);
      });

      it('should return initial array if no FeedingHive is added', () => {
        const feedingHiveCollection: IFeedingHive[] = [sampleWithRequiredData];
        expectedResult = service.addFeedingHiveToCollectionIfMissing(feedingHiveCollection, undefined, null);
        expect(expectedResult).toEqual(feedingHiveCollection);
      });
    });

    describe('compareFeedingHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedingHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedingHive(entity1, entity2);
        const compareResult2 = service.compareFeedingHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedingHive(entity1, entity2);
        const compareResult2 = service.compareFeedingHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedingHive(entity1, entity2);
        const compareResult2 = service.compareFeedingHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
