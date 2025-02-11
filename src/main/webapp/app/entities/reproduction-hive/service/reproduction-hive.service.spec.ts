import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReproductionHive } from '../reproduction-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reproduction-hive.test-samples';

import { ReproductionHiveService, RestReproductionHive } from './reproduction-hive.service';

const requireRestSample: RestReproductionHive = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('ReproductionHive Service', () => {
  let service: ReproductionHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IReproductionHive | IReproductionHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReproductionHiveService);
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

    it('should create a ReproductionHive', () => {
      const reproductionHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reproductionHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReproductionHive', () => {
      const reproductionHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reproductionHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReproductionHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReproductionHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReproductionHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReproductionHiveToCollectionIfMissing', () => {
      it('should add a ReproductionHive to an empty array', () => {
        const reproductionHive: IReproductionHive = sampleWithRequiredData;
        expectedResult = service.addReproductionHiveToCollectionIfMissing([], reproductionHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reproductionHive);
      });

      it('should not add a ReproductionHive to an array that contains it', () => {
        const reproductionHive: IReproductionHive = sampleWithRequiredData;
        const reproductionHiveCollection: IReproductionHive[] = [
          {
            ...reproductionHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReproductionHiveToCollectionIfMissing(reproductionHiveCollection, reproductionHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReproductionHive to an array that doesn't contain it", () => {
        const reproductionHive: IReproductionHive = sampleWithRequiredData;
        const reproductionHiveCollection: IReproductionHive[] = [sampleWithPartialData];
        expectedResult = service.addReproductionHiveToCollectionIfMissing(reproductionHiveCollection, reproductionHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reproductionHive);
      });

      it('should add only unique ReproductionHive to an array', () => {
        const reproductionHiveArray: IReproductionHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reproductionHiveCollection: IReproductionHive[] = [sampleWithRequiredData];
        expectedResult = service.addReproductionHiveToCollectionIfMissing(reproductionHiveCollection, ...reproductionHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reproductionHive: IReproductionHive = sampleWithRequiredData;
        const reproductionHive2: IReproductionHive = sampleWithPartialData;
        expectedResult = service.addReproductionHiveToCollectionIfMissing([], reproductionHive, reproductionHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reproductionHive);
        expect(expectedResult).toContain(reproductionHive2);
      });

      it('should accept null and undefined values', () => {
        const reproductionHive: IReproductionHive = sampleWithRequiredData;
        expectedResult = service.addReproductionHiveToCollectionIfMissing([], null, reproductionHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reproductionHive);
      });

      it('should return initial array if no ReproductionHive is added', () => {
        const reproductionHiveCollection: IReproductionHive[] = [sampleWithRequiredData];
        expectedResult = service.addReproductionHiveToCollectionIfMissing(reproductionHiveCollection, undefined, null);
        expect(expectedResult).toEqual(reproductionHiveCollection);
      });
    });

    describe('compareReproductionHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReproductionHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReproductionHive(entity1, entity2);
        const compareResult2 = service.compareReproductionHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReproductionHive(entity1, entity2);
        const compareResult2 = service.compareReproductionHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReproductionHive(entity1, entity2);
        const compareResult2 = service.compareReproductionHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
