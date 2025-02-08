import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHiveType } from '../hive-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../hive-type.test-samples';

import { HiveTypeService, RestHiveType } from './hive-type.service';

const requireRestSample: RestHiveType = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('HiveType Service', () => {
  let service: HiveTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IHiveType | IHiveType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HiveTypeService);
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

    it('should create a HiveType', () => {
      const hiveType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hiveType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HiveType', () => {
      const hiveType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hiveType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HiveType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HiveType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HiveType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHiveTypeToCollectionIfMissing', () => {
      it('should add a HiveType to an empty array', () => {
        const hiveType: IHiveType = sampleWithRequiredData;
        expectedResult = service.addHiveTypeToCollectionIfMissing([], hiveType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hiveType);
      });

      it('should not add a HiveType to an array that contains it', () => {
        const hiveType: IHiveType = sampleWithRequiredData;
        const hiveTypeCollection: IHiveType[] = [
          {
            ...hiveType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHiveTypeToCollectionIfMissing(hiveTypeCollection, hiveType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HiveType to an array that doesn't contain it", () => {
        const hiveType: IHiveType = sampleWithRequiredData;
        const hiveTypeCollection: IHiveType[] = [sampleWithPartialData];
        expectedResult = service.addHiveTypeToCollectionIfMissing(hiveTypeCollection, hiveType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hiveType);
      });

      it('should add only unique HiveType to an array', () => {
        const hiveTypeArray: IHiveType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hiveTypeCollection: IHiveType[] = [sampleWithRequiredData];
        expectedResult = service.addHiveTypeToCollectionIfMissing(hiveTypeCollection, ...hiveTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hiveType: IHiveType = sampleWithRequiredData;
        const hiveType2: IHiveType = sampleWithPartialData;
        expectedResult = service.addHiveTypeToCollectionIfMissing([], hiveType, hiveType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hiveType);
        expect(expectedResult).toContain(hiveType2);
      });

      it('should accept null and undefined values', () => {
        const hiveType: IHiveType = sampleWithRequiredData;
        expectedResult = service.addHiveTypeToCollectionIfMissing([], null, hiveType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hiveType);
      });

      it('should return initial array if no HiveType is added', () => {
        const hiveTypeCollection: IHiveType[] = [sampleWithRequiredData];
        expectedResult = service.addHiveTypeToCollectionIfMissing(hiveTypeCollection, undefined, null);
        expect(expectedResult).toEqual(hiveTypeCollection);
      });
    });

    describe('compareHiveType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHiveType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHiveType(entity1, entity2);
        const compareResult2 = service.compareHiveType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHiveType(entity1, entity2);
        const compareResult2 = service.compareHiveType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHiveType(entity1, entity2);
        const compareResult2 = service.compareHiveType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
