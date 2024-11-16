import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IApiary } from '../apiary.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../apiary.test-samples';

import { ApiaryService, RestApiary } from './apiary.service';

const requireRestSample: RestApiary = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
};

describe('Apiary Service', () => {
  let service: ApiaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IApiary | IApiary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ApiaryService);
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

    it('should create a Apiary', () => {
      const apiary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apiary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Apiary', () => {
      const apiary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apiary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Apiary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Apiary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Apiary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApiaryToCollectionIfMissing', () => {
      it('should add a Apiary to an empty array', () => {
        const apiary: IApiary = sampleWithRequiredData;
        expectedResult = service.addApiaryToCollectionIfMissing([], apiary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apiary);
      });

      it('should not add a Apiary to an array that contains it', () => {
        const apiary: IApiary = sampleWithRequiredData;
        const apiaryCollection: IApiary[] = [
          {
            ...apiary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApiaryToCollectionIfMissing(apiaryCollection, apiary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Apiary to an array that doesn't contain it", () => {
        const apiary: IApiary = sampleWithRequiredData;
        const apiaryCollection: IApiary[] = [sampleWithPartialData];
        expectedResult = service.addApiaryToCollectionIfMissing(apiaryCollection, apiary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apiary);
      });

      it('should add only unique Apiary to an array', () => {
        const apiaryArray: IApiary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apiaryCollection: IApiary[] = [sampleWithRequiredData];
        expectedResult = service.addApiaryToCollectionIfMissing(apiaryCollection, ...apiaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apiary: IApiary = sampleWithRequiredData;
        const apiary2: IApiary = sampleWithPartialData;
        expectedResult = service.addApiaryToCollectionIfMissing([], apiary, apiary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apiary);
        expect(expectedResult).toContain(apiary2);
      });

      it('should accept null and undefined values', () => {
        const apiary: IApiary = sampleWithRequiredData;
        expectedResult = service.addApiaryToCollectionIfMissing([], null, apiary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apiary);
      });

      it('should return initial array if no Apiary is added', () => {
        const apiaryCollection: IApiary[] = [sampleWithRequiredData];
        expectedResult = service.addApiaryToCollectionIfMissing(apiaryCollection, undefined, null);
        expect(expectedResult).toEqual(apiaryCollection);
      });
    });

    describe('compareApiary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApiary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApiary(entity1, entity2);
        const compareResult2 = service.compareApiary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApiary(entity1, entity2);
        const compareResult2 = service.compareApiary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApiary(entity1, entity2);
        const compareResult2 = service.compareApiary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
