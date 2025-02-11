import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IQueen } from '../queen.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../queen.test-samples';

import { QueenService, RestQueen } from './queen.service';

const requireRestSample: RestQueen = {
  ...sampleWithRequiredData,
  activeFromDate: sampleWithRequiredData.activeFromDate?.toJSON(),
  activeToDate: sampleWithRequiredData.activeToDate?.toJSON(),
  queenChangeDate: sampleWithRequiredData.queenChangeDate?.toJSON(),
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateModified: sampleWithRequiredData.dateModified?.toJSON(),
  dateSynched: sampleWithRequiredData.dateSynched?.toJSON(),
  dateDeleted: sampleWithRequiredData.dateDeleted?.toJSON(),
};

describe('Queen Service', () => {
  let service: QueenService;
  let httpMock: HttpTestingController;
  let expectedResult: IQueen | IQueen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QueenService);
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

    it('should create a Queen', () => {
      const queen = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(queen).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Queen', () => {
      const queen = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(queen).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Queen', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Queen', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Queen', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQueenToCollectionIfMissing', () => {
      it('should add a Queen to an empty array', () => {
        const queen: IQueen = sampleWithRequiredData;
        expectedResult = service.addQueenToCollectionIfMissing([], queen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queen);
      });

      it('should not add a Queen to an array that contains it', () => {
        const queen: IQueen = sampleWithRequiredData;
        const queenCollection: IQueen[] = [
          {
            ...queen,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQueenToCollectionIfMissing(queenCollection, queen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Queen to an array that doesn't contain it", () => {
        const queen: IQueen = sampleWithRequiredData;
        const queenCollection: IQueen[] = [sampleWithPartialData];
        expectedResult = service.addQueenToCollectionIfMissing(queenCollection, queen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queen);
      });

      it('should add only unique Queen to an array', () => {
        const queenArray: IQueen[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const queenCollection: IQueen[] = [sampleWithRequiredData];
        expectedResult = service.addQueenToCollectionIfMissing(queenCollection, ...queenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const queen: IQueen = sampleWithRequiredData;
        const queen2: IQueen = sampleWithPartialData;
        expectedResult = service.addQueenToCollectionIfMissing([], queen, queen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queen);
        expect(expectedResult).toContain(queen2);
      });

      it('should accept null and undefined values', () => {
        const queen: IQueen = sampleWithRequiredData;
        expectedResult = service.addQueenToCollectionIfMissing([], null, queen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queen);
      });

      it('should return initial array if no Queen is added', () => {
        const queenCollection: IQueen[] = [sampleWithRequiredData];
        expectedResult = service.addQueenToCollectionIfMissing(queenCollection, undefined, null);
        expect(expectedResult).toEqual(queenCollection);
      });
    });

    describe('compareQueen', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQueen(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQueen(entity1, entity2);
        const compareResult2 = service.compareQueen(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQueen(entity1, entity2);
        const compareResult2 = service.compareQueen(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQueen(entity1, entity2);
        const compareResult2 = service.compareQueen(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
