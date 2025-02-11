import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHarvestType } from '../harvest-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../harvest-type.test-samples';

import { HarvestTypeService, RestHarvestType } from './harvest-type.service';

const requireRestSample: RestHarvestType = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('HarvestType Service', () => {
  let service: HarvestTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IHarvestType | IHarvestType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HarvestTypeService);
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

    it('should create a HarvestType', () => {
      const harvestType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(harvestType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HarvestType', () => {
      const harvestType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(harvestType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HarvestType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HarvestType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HarvestType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHarvestTypeToCollectionIfMissing', () => {
      it('should add a HarvestType to an empty array', () => {
        const harvestType: IHarvestType = sampleWithRequiredData;
        expectedResult = service.addHarvestTypeToCollectionIfMissing([], harvestType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(harvestType);
      });

      it('should not add a HarvestType to an array that contains it', () => {
        const harvestType: IHarvestType = sampleWithRequiredData;
        const harvestTypeCollection: IHarvestType[] = [
          {
            ...harvestType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHarvestTypeToCollectionIfMissing(harvestTypeCollection, harvestType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HarvestType to an array that doesn't contain it", () => {
        const harvestType: IHarvestType = sampleWithRequiredData;
        const harvestTypeCollection: IHarvestType[] = [sampleWithPartialData];
        expectedResult = service.addHarvestTypeToCollectionIfMissing(harvestTypeCollection, harvestType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(harvestType);
      });

      it('should add only unique HarvestType to an array', () => {
        const harvestTypeArray: IHarvestType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const harvestTypeCollection: IHarvestType[] = [sampleWithRequiredData];
        expectedResult = service.addHarvestTypeToCollectionIfMissing(harvestTypeCollection, ...harvestTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const harvestType: IHarvestType = sampleWithRequiredData;
        const harvestType2: IHarvestType = sampleWithPartialData;
        expectedResult = service.addHarvestTypeToCollectionIfMissing([], harvestType, harvestType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(harvestType);
        expect(expectedResult).toContain(harvestType2);
      });

      it('should accept null and undefined values', () => {
        const harvestType: IHarvestType = sampleWithRequiredData;
        expectedResult = service.addHarvestTypeToCollectionIfMissing([], null, harvestType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(harvestType);
      });

      it('should return initial array if no HarvestType is added', () => {
        const harvestTypeCollection: IHarvestType[] = [sampleWithRequiredData];
        expectedResult = service.addHarvestTypeToCollectionIfMissing(harvestTypeCollection, undefined, null);
        expect(expectedResult).toEqual(harvestTypeCollection);
      });
    });

    describe('compareHarvestType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHarvestType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHarvestType(entity1, entity2);
        const compareResult2 = service.compareHarvestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHarvestType(entity1, entity2);
        const compareResult2 = service.compareHarvestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHarvestType(entity1, entity2);
        const compareResult2 = service.compareHarvestType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
