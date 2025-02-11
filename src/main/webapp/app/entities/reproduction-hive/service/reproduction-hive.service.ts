import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReproductionHive, NewReproductionHive } from '../reproduction-hive.model';

export type PartialUpdateReproductionHive = Partial<IReproductionHive> & Pick<IReproductionHive, 'id'>;

type RestOf<T extends IReproductionHive | NewReproductionHive> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestReproductionHive = RestOf<IReproductionHive>;

export type NewRestReproductionHive = RestOf<NewReproductionHive>;

export type PartialUpdateRestReproductionHive = RestOf<PartialUpdateReproductionHive>;

export type EntityResponseType = HttpResponse<IReproductionHive>;
export type EntityArrayResponseType = HttpResponse<IReproductionHive[]>;

@Injectable({ providedIn: 'root' })
export class ReproductionHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reproduction-hives');

  create(reproductionHive: NewReproductionHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reproductionHive);
    return this.http
      .post<RestReproductionHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reproductionHive: IReproductionHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reproductionHive);
    return this.http
      .put<RestReproductionHive>(`${this.resourceUrl}/${this.getReproductionHiveIdentifier(reproductionHive)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reproductionHive: PartialUpdateReproductionHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reproductionHive);
    return this.http
      .patch<RestReproductionHive>(`${this.resourceUrl}/${this.getReproductionHiveIdentifier(reproductionHive)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReproductionHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReproductionHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReproductionHiveIdentifier(reproductionHive: Pick<IReproductionHive, 'id'>): number {
    return reproductionHive.id;
  }

  compareReproductionHive(o1: Pick<IReproductionHive, 'id'> | null, o2: Pick<IReproductionHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getReproductionHiveIdentifier(o1) === this.getReproductionHiveIdentifier(o2) : o1 === o2;
  }

  addReproductionHiveToCollectionIfMissing<Type extends Pick<IReproductionHive, 'id'>>(
    reproductionHiveCollection: Type[],
    ...reproductionHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reproductionHives: Type[] = reproductionHivesToCheck.filter(isPresent);
    if (reproductionHives.length > 0) {
      const reproductionHiveCollectionIdentifiers = reproductionHiveCollection.map(reproductionHiveItem =>
        this.getReproductionHiveIdentifier(reproductionHiveItem),
      );
      const reproductionHivesToAdd = reproductionHives.filter(reproductionHiveItem => {
        const reproductionHiveIdentifier = this.getReproductionHiveIdentifier(reproductionHiveItem);
        if (reproductionHiveCollectionIdentifiers.includes(reproductionHiveIdentifier)) {
          return false;
        }
        reproductionHiveCollectionIdentifiers.push(reproductionHiveIdentifier);
        return true;
      });
      return [...reproductionHivesToAdd, ...reproductionHiveCollection];
    }
    return reproductionHiveCollection;
  }

  protected convertDateFromClient<T extends IReproductionHive | NewReproductionHive | PartialUpdateReproductionHive>(
    reproductionHive: T,
  ): RestOf<T> {
    return {
      ...reproductionHive,
      dateCreated: reproductionHive.dateCreated?.toJSON() ?? null,
      dateModified: reproductionHive.dateModified?.toJSON() ?? null,
      dateSynched: reproductionHive.dateSynched?.toJSON() ?? null,
      dateDeleted: reproductionHive.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReproductionHive: RestReproductionHive): IReproductionHive {
    return {
      ...restReproductionHive,
      dateCreated: restReproductionHive.dateCreated ? dayjs(restReproductionHive.dateCreated) : undefined,
      dateModified: restReproductionHive.dateModified ? dayjs(restReproductionHive.dateModified) : undefined,
      dateSynched: restReproductionHive.dateSynched ? dayjs(restReproductionHive.dateSynched) : undefined,
      dateDeleted: restReproductionHive.dateDeleted ? dayjs(restReproductionHive.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReproductionHive>): HttpResponse<IReproductionHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReproductionHive[]>): HttpResponse<IReproductionHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
