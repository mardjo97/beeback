import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQueen, NewQueen } from '../queen.model';

export type PartialUpdateQueen = Partial<IQueen> & Pick<IQueen, 'id'>;

type RestOf<T extends IQueen | NewQueen> = Omit<
  T,
  'activeFromDate' | 'activeToDate' | 'queenChangeDate' | 'dateCreated' | 'dateModified' | 'dateSynched'
> & {
  activeFromDate?: string | null;
  activeToDate?: string | null;
  queenChangeDate?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
};

export type RestQueen = RestOf<IQueen>;

export type NewRestQueen = RestOf<NewQueen>;

export type PartialUpdateRestQueen = RestOf<PartialUpdateQueen>;

export type EntityResponseType = HttpResponse<IQueen>;
export type EntityArrayResponseType = HttpResponse<IQueen[]>;

@Injectable({ providedIn: 'root' })
export class QueenService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/queens');

  create(queen: NewQueen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queen);
    return this.http.post<RestQueen>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(queen: IQueen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queen);
    return this.http
      .put<RestQueen>(`${this.resourceUrl}/${this.getQueenIdentifier(queen)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(queen: PartialUpdateQueen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queen);
    return this.http
      .patch<RestQueen>(`${this.resourceUrl}/${this.getQueenIdentifier(queen)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQueen>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQueen[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQueenIdentifier(queen: Pick<IQueen, 'id'>): number {
    return queen.id;
  }

  compareQueen(o1: Pick<IQueen, 'id'> | null, o2: Pick<IQueen, 'id'> | null): boolean {
    return o1 && o2 ? this.getQueenIdentifier(o1) === this.getQueenIdentifier(o2) : o1 === o2;
  }

  addQueenToCollectionIfMissing<Type extends Pick<IQueen, 'id'>>(
    queenCollection: Type[],
    ...queensToCheck: (Type | null | undefined)[]
  ): Type[] {
    const queens: Type[] = queensToCheck.filter(isPresent);
    if (queens.length > 0) {
      const queenCollectionIdentifiers = queenCollection.map(queenItem => this.getQueenIdentifier(queenItem));
      const queensToAdd = queens.filter(queenItem => {
        const queenIdentifier = this.getQueenIdentifier(queenItem);
        if (queenCollectionIdentifiers.includes(queenIdentifier)) {
          return false;
        }
        queenCollectionIdentifiers.push(queenIdentifier);
        return true;
      });
      return [...queensToAdd, ...queenCollection];
    }
    return queenCollection;
  }

  protected convertDateFromClient<T extends IQueen | NewQueen | PartialUpdateQueen>(queen: T): RestOf<T> {
    return {
      ...queen,
      activeFromDate: queen.activeFromDate?.toJSON() ?? null,
      activeToDate: queen.activeToDate?.toJSON() ?? null,
      queenChangeDate: queen.queenChangeDate?.toJSON() ?? null,
      dateCreated: queen.dateCreated?.toJSON() ?? null,
      dateModified: queen.dateModified?.toJSON() ?? null,
      dateSynched: queen.dateSynched?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQueen: RestQueen): IQueen {
    return {
      ...restQueen,
      activeFromDate: restQueen.activeFromDate ? dayjs(restQueen.activeFromDate) : undefined,
      activeToDate: restQueen.activeToDate ? dayjs(restQueen.activeToDate) : undefined,
      queenChangeDate: restQueen.queenChangeDate ? dayjs(restQueen.queenChangeDate) : undefined,
      dateCreated: restQueen.dateCreated ? dayjs(restQueen.dateCreated) : undefined,
      dateModified: restQueen.dateModified ? dayjs(restQueen.dateModified) : undefined,
      dateSynched: restQueen.dateSynched ? dayjs(restQueen.dateSynched) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQueen>): HttpResponse<IQueen> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQueen[]>): HttpResponse<IQueen[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
