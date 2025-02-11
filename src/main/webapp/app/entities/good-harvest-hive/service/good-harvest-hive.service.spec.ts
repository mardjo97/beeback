import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGoodHarvestHive } from '../good-harvest-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../good-harvest-hive.test-samples';

import { GoodHarvestHiveService, RestGoodHarvestHive } from './good-harvest-hive.service';

const requireRestSample: RestGoodHarvestHive = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('GoodHarvestHive Service', () => {
  let service: GoodHarvestHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IGoodHarvestHive | IGoodHarvestHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GoodHarvestHiveService);
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

    it('should create a GoodHarvestHive', () => {
      const goodHarvestHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(goodHarvestHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GoodHarvestHive', () => {
      const goodHarvestHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(goodHarvestHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GoodHarvestHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GoodHarvestHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GoodHarvestHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGoodHarvestHiveToCollectionIfMissing', () => {
      it('should add a GoodHarvestHive to an empty array', () => {
        const goodHarvestHive: IGoodHarvestHive = sampleWithRequiredData;
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing([], goodHarvestHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(goodHarvestHive);
      });

      it('should not add a GoodHarvestHive to an array that contains it', () => {
        const goodHarvestHive: IGoodHarvestHive = sampleWithRequiredData;
        const goodHarvestHiveCollection: IGoodHarvestHive[] = [
          {
            ...goodHarvestHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing(goodHarvestHiveCollection, goodHarvestHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GoodHarvestHive to an array that doesn't contain it", () => {
        const goodHarvestHive: IGoodHarvestHive = sampleWithRequiredData;
        const goodHarvestHiveCollection: IGoodHarvestHive[] = [sampleWithPartialData];
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing(goodHarvestHiveCollection, goodHarvestHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(goodHarvestHive);
      });

      it('should add only unique GoodHarvestHive to an array', () => {
        const goodHarvestHiveArray: IGoodHarvestHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const goodHarvestHiveCollection: IGoodHarvestHive[] = [sampleWithRequiredData];
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing(goodHarvestHiveCollection, ...goodHarvestHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const goodHarvestHive: IGoodHarvestHive = sampleWithRequiredData;
        const goodHarvestHive2: IGoodHarvestHive = sampleWithPartialData;
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing([], goodHarvestHive, goodHarvestHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(goodHarvestHive);
        expect(expectedResult).toContain(goodHarvestHive2);
      });

      it('should accept null and undefined values', () => {
        const goodHarvestHive: IGoodHarvestHive = sampleWithRequiredData;
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing([], null, goodHarvestHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(goodHarvestHive);
      });

      it('should return initial array if no GoodHarvestHive is added', () => {
        const goodHarvestHiveCollection: IGoodHarvestHive[] = [sampleWithRequiredData];
        expectedResult = service.addGoodHarvestHiveToCollectionIfMissing(goodHarvestHiveCollection, undefined, null);
        expect(expectedResult).toEqual(goodHarvestHiveCollection);
      });
    });

    describe('compareGoodHarvestHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGoodHarvestHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGoodHarvestHive(entity1, entity2);
        const compareResult2 = service.compareGoodHarvestHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGoodHarvestHive(entity1, entity2);
        const compareResult2 = service.compareGoodHarvestHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGoodHarvestHive(entity1, entity2);
        const compareResult2 = service.compareGoodHarvestHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
