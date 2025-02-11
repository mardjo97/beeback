import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHarvest } from '../harvest.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../harvest.test-samples';

import { HarvestService, RestHarvest } from './harvest.service';

const requireRestSample: RestHarvest = {
  ...sampleWithRequiredData,
  dateCollected: sampleWithRequiredData.dateCollected?.toJSON(),
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('Harvest Service', () => {
  let service: HarvestService;
  let httpMock: HttpTestingController;
  let expectedResult: IHarvest | IHarvest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HarvestService);
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

    it('should create a Harvest', () => {
      const harvest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(harvest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Harvest', () => {
      const harvest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(harvest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Harvest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Harvest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Harvest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHarvestToCollectionIfMissing', () => {
      it('should add a Harvest to an empty array', () => {
        const harvest: IHarvest = sampleWithRequiredData;
        expectedResult = service.addHarvestToCollectionIfMissing([], harvest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(harvest);
      });

      it('should not add a Harvest to an array that contains it', () => {
        const harvest: IHarvest = sampleWithRequiredData;
        const harvestCollection: IHarvest[] = [
          {
            ...harvest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHarvestToCollectionIfMissing(harvestCollection, harvest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Harvest to an array that doesn't contain it", () => {
        const harvest: IHarvest = sampleWithRequiredData;
        const harvestCollection: IHarvest[] = [sampleWithPartialData];
        expectedResult = service.addHarvestToCollectionIfMissing(harvestCollection, harvest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(harvest);
      });

      it('should add only unique Harvest to an array', () => {
        const harvestArray: IHarvest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const harvestCollection: IHarvest[] = [sampleWithRequiredData];
        expectedResult = service.addHarvestToCollectionIfMissing(harvestCollection, ...harvestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const harvest: IHarvest = sampleWithRequiredData;
        const harvest2: IHarvest = sampleWithPartialData;
        expectedResult = service.addHarvestToCollectionIfMissing([], harvest, harvest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(harvest);
        expect(expectedResult).toContain(harvest2);
      });

      it('should accept null and undefined values', () => {
        const harvest: IHarvest = sampleWithRequiredData;
        expectedResult = service.addHarvestToCollectionIfMissing([], null, harvest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(harvest);
      });

      it('should return initial array if no Harvest is added', () => {
        const harvestCollection: IHarvest[] = [sampleWithRequiredData];
        expectedResult = service.addHarvestToCollectionIfMissing(harvestCollection, undefined, null);
        expect(expectedResult).toEqual(harvestCollection);
      });
    });

    describe('compareHarvest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHarvest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHarvest(entity1, entity2);
        const compareResult2 = service.compareHarvest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHarvest(entity1, entity2);
        const compareResult2 = service.compareHarvest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHarvest(entity1, entity2);
        const compareResult2 = service.compareHarvest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
