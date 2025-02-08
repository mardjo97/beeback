import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHive } from '../hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../hive.test-samples';

import { HiveService, RestHive } from './hive.service';

const requireRestSample: RestHive = {
  ...sampleWithRequiredData,
  examinationDate: sampleWithRequiredData.examinationDate?.toJSON(),
  archivedDate: sampleWithRequiredData.archivedDate?.toJSON(),
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('Hive Service', () => {
  let service: HiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IHive | IHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HiveService);
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

    it('should create a Hive', () => {
      const hive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hive', () => {
      const hive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Hive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHiveToCollectionIfMissing', () => {
      it('should add a Hive to an empty array', () => {
        const hive: IHive = sampleWithRequiredData;
        expectedResult = service.addHiveToCollectionIfMissing([], hive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hive);
      });

      it('should not add a Hive to an array that contains it', () => {
        const hive: IHive = sampleWithRequiredData;
        const hiveCollection: IHive[] = [
          {
            ...hive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHiveToCollectionIfMissing(hiveCollection, hive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hive to an array that doesn't contain it", () => {
        const hive: IHive = sampleWithRequiredData;
        const hiveCollection: IHive[] = [sampleWithPartialData];
        expectedResult = service.addHiveToCollectionIfMissing(hiveCollection, hive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hive);
      });

      it('should add only unique Hive to an array', () => {
        const hiveArray: IHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hiveCollection: IHive[] = [sampleWithRequiredData];
        expectedResult = service.addHiveToCollectionIfMissing(hiveCollection, ...hiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hive: IHive = sampleWithRequiredData;
        const hive2: IHive = sampleWithPartialData;
        expectedResult = service.addHiveToCollectionIfMissing([], hive, hive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hive);
        expect(expectedResult).toContain(hive2);
      });

      it('should accept null and undefined values', () => {
        const hive: IHive = sampleWithRequiredData;
        expectedResult = service.addHiveToCollectionIfMissing([], null, hive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hive);
      });

      it('should return initial array if no Hive is added', () => {
        const hiveCollection: IHive[] = [sampleWithRequiredData];
        expectedResult = service.addHiveToCollectionIfMissing(hiveCollection, undefined, null);
        expect(expectedResult).toEqual(hiveCollection);
      });
    });

    describe('compareHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHive(entity1, entity2);
        const compareResult2 = service.compareHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHive(entity1, entity2);
        const compareResult2 = service.compareHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHive(entity1, entity2);
        const compareResult2 = service.compareHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
