import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQueenChangeHive, NewQueenChangeHive } from '../queen-change-hive.model';

export type PartialUpdateQueenChangeHive = Partial<IQueenChangeHive> & Pick<IQueenChangeHive, 'id'>;

type RestOf<T extends IQueenChangeHive | NewQueenChangeHive> = Omit<
  T,
  'dateQueenChange' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  dateQueenChange?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestQueenChangeHive = RestOf<IQueenChangeHive>;

export type NewRestQueenChangeHive = RestOf<NewQueenChangeHive>;

export type PartialUpdateRestQueenChangeHive = RestOf<PartialUpdateQueenChangeHive>;

export type EntityResponseType = HttpResponse<IQueenChangeHive>;
export type EntityArrayResponseType = HttpResponse<IQueenChangeHive[]>;

@Injectable({ providedIn: 'root' })
export class QueenChangeHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/queen-change-hives');

  create(queenChangeHive: NewQueenChangeHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queenChangeHive);
    return this.http
      .post<RestQueenChangeHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(queenChangeHive: IQueenChangeHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queenChangeHive);
    return this.http
      .put<RestQueenChangeHive>(`${this.resourceUrl}/${this.getQueenChangeHiveIdentifier(queenChangeHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(queenChangeHive: PartialUpdateQueenChangeHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(queenChangeHive);
    return this.http
      .patch<RestQueenChangeHive>(`${this.resourceUrl}/${this.getQueenChangeHiveIdentifier(queenChangeHive)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQueenChangeHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQueenChangeHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQueenChangeHiveIdentifier(queenChangeHive: Pick<IQueenChangeHive, 'id'>): number {
    return queenChangeHive.id;
  }

  compareQueenChangeHive(o1: Pick<IQueenChangeHive, 'id'> | null, o2: Pick<IQueenChangeHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getQueenChangeHiveIdentifier(o1) === this.getQueenChangeHiveIdentifier(o2) : o1 === o2;
  }

  addQueenChangeHiveToCollectionIfMissing<Type extends Pick<IQueenChangeHive, 'id'>>(
    queenChangeHiveCollection: Type[],
    ...queenChangeHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const queenChangeHives: Type[] = queenChangeHivesToCheck.filter(isPresent);
    if (queenChangeHives.length > 0) {
      const queenChangeHiveCollectionIdentifiers = queenChangeHiveCollection.map(queenChangeHiveItem =>
        this.getQueenChangeHiveIdentifier(queenChangeHiveItem),
      );
      const queenChangeHivesToAdd = queenChangeHives.filter(queenChangeHiveItem => {
        const queenChangeHiveIdentifier = this.getQueenChangeHiveIdentifier(queenChangeHiveItem);
        if (queenChangeHiveCollectionIdentifiers.includes(queenChangeHiveIdentifier)) {
          return false;
        }
        queenChangeHiveCollectionIdentifiers.push(queenChangeHiveIdentifier);
        return true;
      });
      return [...queenChangeHivesToAdd, ...queenChangeHiveCollection];
    }
    return queenChangeHiveCollection;
  }

  protected convertDateFromClient<T extends IQueenChangeHive | NewQueenChangeHive | PartialUpdateQueenChangeHive>(
    queenChangeHive: T,
  ): RestOf<T> {
    return {
      ...queenChangeHive,
      dateQueenChange: queenChangeHive.dateQueenChange?.toJSON() ?? null,
      dateCreated: queenChangeHive.dateCreated?.toJSON() ?? null,
      dateModified: queenChangeHive.dateModified?.toJSON() ?? null,
      dateSynched: queenChangeHive.dateSynched?.toJSON() ?? null,
      dateDeleted: queenChangeHive.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQueenChangeHive: RestQueenChangeHive): IQueenChangeHive {
    return {
      ...restQueenChangeHive,
      dateQueenChange: restQueenChangeHive.dateQueenChange ? dayjs(restQueenChangeHive.dateQueenChange) : undefined,
      dateCreated: restQueenChangeHive.dateCreated ? dayjs(restQueenChangeHive.dateCreated) : undefined,
      dateModified: restQueenChangeHive.dateModified ? dayjs(restQueenChangeHive.dateModified) : undefined,
      dateSynched: restQueenChangeHive.dateSynched ? dayjs(restQueenChangeHive.dateSynched) : undefined,
      dateDeleted: restQueenChangeHive.dateDeleted ? dayjs(restQueenChangeHive.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQueenChangeHive>): HttpResponse<IQueenChangeHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQueenChangeHive[]>): HttpResponse<IQueenChangeHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
