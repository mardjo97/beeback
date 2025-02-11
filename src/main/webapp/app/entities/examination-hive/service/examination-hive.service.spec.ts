import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExaminationHive } from '../examination-hive.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../examination-hive.test-samples';

import { ExaminationHiveService, RestExaminationHive } from './examination-hive.service';

const requireRestSample: RestExaminationHive = {
  ...sampleWithRequiredData,
  dateExamination: sampleWithRequiredData.dateExamination?.toJSON(),
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('ExaminationHive Service', () => {
  let service: ExaminationHiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IExaminationHive | IExaminationHive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExaminationHiveService);
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

    it('should create a ExaminationHive', () => {
      const examinationHive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(examinationHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExaminationHive', () => {
      const examinationHive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(examinationHive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExaminationHive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExaminationHive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExaminationHive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExaminationHiveToCollectionIfMissing', () => {
      it('should add a ExaminationHive to an empty array', () => {
        const examinationHive: IExaminationHive = sampleWithRequiredData;
        expectedResult = service.addExaminationHiveToCollectionIfMissing([], examinationHive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(examinationHive);
      });

      it('should not add a ExaminationHive to an array that contains it', () => {
        const examinationHive: IExaminationHive = sampleWithRequiredData;
        const examinationHiveCollection: IExaminationHive[] = [
          {
            ...examinationHive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExaminationHiveToCollectionIfMissing(examinationHiveCollection, examinationHive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExaminationHive to an array that doesn't contain it", () => {
        const examinationHive: IExaminationHive = sampleWithRequiredData;
        const examinationHiveCollection: IExaminationHive[] = [sampleWithPartialData];
        expectedResult = service.addExaminationHiveToCollectionIfMissing(examinationHiveCollection, examinationHive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(examinationHive);
      });

      it('should add only unique ExaminationHive to an array', () => {
        const examinationHiveArray: IExaminationHive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const examinationHiveCollection: IExaminationHive[] = [sampleWithRequiredData];
        expectedResult = service.addExaminationHiveToCollectionIfMissing(examinationHiveCollection, ...examinationHiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const examinationHive: IExaminationHive = sampleWithRequiredData;
        const examinationHive2: IExaminationHive = sampleWithPartialData;
        expectedResult = service.addExaminationHiveToCollectionIfMissing([], examinationHive, examinationHive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(examinationHive);
        expect(expectedResult).toContain(examinationHive2);
      });

      it('should accept null and undefined values', () => {
        const examinationHive: IExaminationHive = sampleWithRequiredData;
        expectedResult = service.addExaminationHiveToCollectionIfMissing([], null, examinationHive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(examinationHive);
      });

      it('should return initial array if no ExaminationHive is added', () => {
        const examinationHiveCollection: IExaminationHive[] = [sampleWithRequiredData];
        expectedResult = service.addExaminationHiveToCollectionIfMissing(examinationHiveCollection, undefined, null);
        expect(expectedResult).toEqual(examinationHiveCollection);
      });
    });

    describe('compareExaminationHive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExaminationHive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExaminationHive(entity1, entity2);
        const compareResult2 = service.compareExaminationHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExaminationHive(entity1, entity2);
        const compareResult2 = service.compareExaminationHive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExaminationHive(entity1, entity2);
        const compareResult2 = service.compareExaminationHive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
