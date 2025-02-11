import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHiveLocation } from '../hive-location.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../hive-location.test-samples';

import { HiveLocationService, RestHiveLocation } from './hive-location.service';

const requireRestSample: RestHiveLocation = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('HiveLocation Service', () => {
  let service: HiveLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IHiveLocation | IHiveLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HiveLocationService);
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

    it('should create a HiveLocation', () => {
      const hiveLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hiveLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HiveLocation', () => {
      const hiveLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hiveLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HiveLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HiveLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HiveLocation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHiveLocationToCollectionIfMissing', () => {
      it('should add a HiveLocation to an empty array', () => {
        const hiveLocation: IHiveLocation = sampleWithRequiredData;
        expectedResult = service.addHiveLocationToCollectionIfMissing([], hiveLocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hiveLocation);
      });

      it('should not add a HiveLocation to an array that contains it', () => {
        const hiveLocation: IHiveLocation = sampleWithRequiredData;
        const hiveLocationCollection: IHiveLocation[] = [
          {
            ...hiveLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHiveLocationToCollectionIfMissing(hiveLocationCollection, hiveLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HiveLocation to an array that doesn't contain it", () => {
        const hiveLocation: IHiveLocation = sampleWithRequiredData;
        const hiveLocationCollection: IHiveLocation[] = [sampleWithPartialData];
        expectedResult = service.addHiveLocationToCollectionIfMissing(hiveLocationCollection, hiveLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hiveLocation);
      });

      it('should add only unique HiveLocation to an array', () => {
        const hiveLocationArray: IHiveLocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hiveLocationCollection: IHiveLocation[] = [sampleWithRequiredData];
        expectedResult = service.addHiveLocationToCollectionIfMissing(hiveLocationCollection, ...hiveLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hiveLocation: IHiveLocation = sampleWithRequiredData;
        const hiveLocation2: IHiveLocation = sampleWithPartialData;
        expectedResult = service.addHiveLocationToCollectionIfMissing([], hiveLocation, hiveLocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hiveLocation);
        expect(expectedResult).toContain(hiveLocation2);
      });

      it('should accept null and undefined values', () => {
        const hiveLocation: IHiveLocation = sampleWithRequiredData;
        expectedResult = service.addHiveLocationToCollectionIfMissing([], null, hiveLocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hiveLocation);
      });

      it('should return initial array if no HiveLocation is added', () => {
        const hiveLocationCollection: IHiveLocation[] = [sampleWithRequiredData];
        expectedResult = service.addHiveLocationToCollectionIfMissing(hiveLocationCollection, undefined, null);
        expect(expectedResult).toEqual(hiveLocationCollection);
      });
    });

    describe('compareHiveLocation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHiveLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHiveLocation(entity1, entity2);
        const compareResult2 = service.compareHiveLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHiveLocation(entity1, entity2);
        const compareResult2 = service.compareHiveLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHiveLocation(entity1, entity2);
        const compareResult2 = service.compareHiveLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
