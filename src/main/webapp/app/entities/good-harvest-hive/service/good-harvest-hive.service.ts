import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGoodHarvestHive, NewGoodHarvestHive } from '../good-harvest-hive.model';

export type PartialUpdateGoodHarvestHive = Partial<IGoodHarvestHive> & Pick<IGoodHarvestHive, 'id'>;

type RestOf<T extends IGoodHarvestHive | NewGoodHarvestHive> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestGoodHarvestHive = RestOf<IGoodHarvestHive>;

export type NewRestGoodHarvestHive = RestOf<NewGoodHarvestHive>;

export type PartialUpdateRestGoodHarvestHive = RestOf<PartialUpdateGoodHarvestHive>;

export type EntityResponseType = HttpResponse<IGoodHarvestHive>;
export type EntityArrayResponseType = HttpResponse<IGoodHarvestHive[]>;

@Injectable({ providedIn: 'root' })
export class GoodHarvestHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/good-harvest-hives');

  create(goodHarvestHive: NewGoodHarvestHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goodHarvestHive);
    return this.http
      .post<RestGoodHarvestHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(goodHarvestHive: IGoodHarvestHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goodHarvestHive);
    return this.http
      .put<RestGoodHarvestHive>(`${this.resourceUrl}/${this.getGoodHarvestHiveIdentifier(goodHarvestHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(goodHarvestHive: PartialUpdateGoodHarvestHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goodHarvestHive);
    return this.http
      .patch<RestGoodHarvestHive>(`${this.resourceUrl}/${this.getGoodHarvestHiveIdentifier(goodHarvestHive)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestGoodHarvestHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGoodHarvestHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGoodHarvestHiveIdentifier(goodHarvestHive: Pick<IGoodHarvestHive, 'id'>): number {
    return goodHarvestHive.id;
  }

  compareGoodHarvestHive(o1: Pick<IGoodHarvestHive, 'id'> | null, o2: Pick<IGoodHarvestHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getGoodHarvestHiveIdentifier(o1) === this.getGoodHarvestHiveIdentifier(o2) : o1 === o2;
  }

  addGoodHarvestHiveToCollectionIfMissing<Type extends Pick<IGoodHarvestHive, 'id'>>(
    goodHarvestHiveCollection: Type[],
    ...goodHarvestHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const goodHarvestHives: Type[] = goodHarvestHivesToCheck.filter(isPresent);
    if (goodHarvestHives.length > 0) {
      const goodHarvestHiveCollectionIdentifiers = goodHarvestHiveCollection.map(goodHarvestHiveItem =>
        this.getGoodHarvestHiveIdentifier(goodHarvestHiveItem),
      );
      const goodHarvestHivesToAdd = goodHarvestHives.filter(goodHarvestHiveItem => {
        const goodHarvestHiveIdentifier = this.getGoodHarvestHiveIdentifier(goodHarvestHiveItem);
        if (goodHarvestHiveCollectionIdentifiers.includes(goodHarvestHiveIdentifier)) {
          return false;
        }
        goodHarvestHiveCollectionIdentifiers.push(goodHarvestHiveIdentifier);
        return true;
      });
      return [...goodHarvestHivesToAdd, ...goodHarvestHiveCollection];
    }
    return goodHarvestHiveCollection;
  }

  protected convertDateFromClient<T extends IGoodHarvestHive | NewGoodHarvestHive | PartialUpdateGoodHarvestHive>(
    goodHarvestHive: T,
  ): RestOf<T> {
    return {
      ...goodHarvestHive,
      dateCreated: goodHarvestHive.dateCreated?.toJSON() ?? null,
      dateModified: goodHarvestHive.dateModified?.toJSON() ?? null,
      dateSynched: goodHarvestHive.dateSynched?.toJSON() ?? null,
      dateDeleted: goodHarvestHive.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restGoodHarvestHive: RestGoodHarvestHive): IGoodHarvestHive {
    return {
      ...restGoodHarvestHive,
      dateCreated: restGoodHarvestHive.dateCreated ? dayjs(restGoodHarvestHive.dateCreated) : undefined,
      dateModified: restGoodHarvestHive.dateModified ? dayjs(restGoodHarvestHive.dateModified) : undefined,
      dateSynched: restGoodHarvestHive.dateSynched ? dayjs(restGoodHarvestHive.dateSynched) : undefined,
      dateDeleted: restGoodHarvestHive.dateDeleted ? dayjs(restGoodHarvestHive.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGoodHarvestHive>): HttpResponse<IGoodHarvestHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGoodHarvestHive[]>): HttpResponse<IGoodHarvestHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
