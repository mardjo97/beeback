import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IQueenChangeHive } from '../queen-change-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../queen-change-hive.test-samples';

import { QueenChangeHiveService, RestQueenChangeHive } from './queen-change-hive.service';

const requireRestSample: RestQueenChangeHive = {
  ...sampleWithRequiredData,
  dateQueenChange: sampleWithRequiredData.dateQueenChange?.toJSON(),
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('QueenChangeHive Service', () => {
  let service: QueenChangeHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IQueenChangeHive | IQueenChangeHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QueenChangeHiveService);
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

    it('should create a QueenChangeHive', () => {
      const queenChangeHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(queenChangeHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QueenChangeHive', () => {
      const queenChangeHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(queenChangeHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QueenChangeHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QueenChangeHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QueenChangeHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQueenChangeHiveToCollectionIfMissing', () => {
      it('should add a QueenChangeHive to an empty array', () => {
        const queenChangeHive: IQueenChangeHive = sampleWithRequiredData;
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing([], queenChangeHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queenChangeHive);
      });

      it('should not add a QueenChangeHive to an array that contains it', () => {
        const queenChangeHive: IQueenChangeHive = sampleWithRequiredData;
        const queenChangeHiveCollection: IQueenChangeHive[] = [
          {
            ...queenChangeHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing(queenChangeHiveCollection, queenChangeHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QueenChangeHive to an array that doesn't contain it", () => {
        const queenChangeHive: IQueenChangeHive = sampleWithRequiredData;
        const queenChangeHiveCollection: IQueenChangeHive[] = [sampleWithPartialData];
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing(queenChangeHiveCollection, queenChangeHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queenChangeHive);
      });

      it('should add only unique QueenChangeHive to an array', () => {
        const queenChangeHiveArray: IQueenChangeHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const queenChangeHiveCollection: IQueenChangeHive[] = [sampleWithRequiredData];
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing(queenChangeHiveCollection, ...queenChangeHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const queenChangeHive: IQueenChangeHive = sampleWithRequiredData;
        const queenChangeHive2: IQueenChangeHive = sampleWithPartialData;
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing([], queenChangeHive, queenChangeHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queenChangeHive);
        expect(expectedResult).toContain(queenChangeHive2);
      });

      it('should accept null and undefined values', () => {
        const queenChangeHive: IQueenChangeHive = sampleWithRequiredData;
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing([], null, queenChangeHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queenChangeHive);
      });

      it('should return initial array if no QueenChangeHive is added', () => {
        const queenChangeHiveCollection: IQueenChangeHive[] = [sampleWithRequiredData];
        expectedResult = service.addQueenChangeHiveToCollectionIfMissing(queenChangeHiveCollection, undefined, null);
        expect(expectedResult).toEqual(queenChangeHiveCollection);
      });
    });

    describe('compareQueenChangeHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQueenChangeHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQueenChangeHive(entity1, entity2);
        const compareResult2 = service.compareQueenChangeHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQueenChangeHive(entity1, entity2);
        const compareResult2 = service.compareQueenChangeHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQueenChangeHive(entity1, entity2);
        const compareResult2 = service.compareQueenChangeHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
