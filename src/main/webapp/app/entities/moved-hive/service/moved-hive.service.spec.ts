import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMovedHive } from '../moved-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../moved-hive.test-samples';

import { MovedHiveService, RestMovedHive } from './moved-hive.service';

const requireRestSample: RestMovedHive = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('MovedHive Service', () => {
  let service: MovedHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IMovedHive | IMovedHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MovedHiveService);
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

    it('should create a MovedHive', () => {
      const movedHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(movedHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MovedHive', () => {
      const movedHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(movedHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MovedHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MovedHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MovedHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMovedHiveToCollectionIfMissing', () => {
      it('should add a MovedHive to an empty array', () => {
        const movedHive: IMovedHive = sampleWithRequiredData;
        expectedResult = service.addMovedHiveToCollectionIfMissing([], movedHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movedHive);
      });

      it('should not add a MovedHive to an array that contains it', () => {
        const movedHive: IMovedHive = sampleWithRequiredData;
        const movedHiveCollection: IMovedHive[] = [
          {
            ...movedHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMovedHiveToCollectionIfMissing(movedHiveCollection, movedHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MovedHive to an array that doesn't contain it", () => {
        const movedHive: IMovedHive = sampleWithRequiredData;
        const movedHiveCollection: IMovedHive[] = [sampleWithPartialData];
        expectedResult = service.addMovedHiveToCollectionIfMissing(movedHiveCollection, movedHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movedHive);
      });

      it('should add only unique MovedHive to an array', () => {
        const movedHiveArray: IMovedHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const movedHiveCollection: IMovedHive[] = [sampleWithRequiredData];
        expectedResult = service.addMovedHiveToCollectionIfMissing(movedHiveCollection, ...movedHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const movedHive: IMovedHive = sampleWithRequiredData;
        const movedHive2: IMovedHive = sampleWithPartialData;
        expectedResult = service.addMovedHiveToCollectionIfMissing([], movedHive, movedHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movedHive);
        expect(expectedResult).toContain(movedHive2);
      });

      it('should accept null and undefined values', () => {
        const movedHive: IMovedHive = sampleWithRequiredData;
        expectedResult = service.addMovedHiveToCollectionIfMissing([], null, movedHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movedHive);
      });

      it('should return initial array if no MovedHive is added', () => {
        const movedHiveCollection: IMovedHive[] = [sampleWithRequiredData];
        expectedResult = service.addMovedHiveToCollectionIfMissing(movedHiveCollection, undefined, null);
        expect(expectedResult).toEqual(movedHiveCollection);
      });
    });

    describe('compareMovedHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMovedHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMovedHive(entity1, entity2);
        const compareResult2 = service.compareMovedHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMovedHive(entity1, entity2);
        const compareResult2 = service.compareMovedHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMovedHive(entity1, entity2);
        const compareResult2 = service.compareMovedHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
