import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHarvestType, NewHarvestType } from '../harvest-type.model';

export type PartialUpdateHarvestType = Partial<IHarvestType> & Pick<IHarvestType, 'id'>;

type RestOf<T extends IHarvestType | NewHarvestType> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestHarvestType = RestOf<IHarvestType>;

export type NewRestHarvestType = RestOf<NewHarvestType>;

export type PartialUpdateRestHarvestType = RestOf<PartialUpdateHarvestType>;

export type EntityResponseType = HttpResponse<IHarvestType>;
export type EntityArrayResponseType = HttpResponse<IHarvestType[]>;

@Injectable({ providedIn: 'root' })
export class HarvestTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/harvest-types');

  create(harvestType: NewHarvestType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvestType);
    return this.http
      .post<RestHarvestType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(harvestType: IHarvestType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvestType);
    return this.http
      .put<RestHarvestType>(`${this.resourceUrl}/${this.getHarvestTypeIdentifier(harvestType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(harvestType: PartialUpdateHarvestType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvestType);
    return this.http
      .patch<RestHarvestType>(`${this.resourceUrl}/${this.getHarvestTypeIdentifier(harvestType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHarvestType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHarvestType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHarvestTypeIdentifier(harvestType: Pick<IHarvestType, 'id'>): number {
    return harvestType.id;
  }

  compareHarvestType(o1: Pick<IHarvestType, 'id'> | null, o2: Pick<IHarvestType, 'id'> | null): boolean {
    return o1 && o2 ? this.getHarvestTypeIdentifier(o1) === this.getHarvestTypeIdentifier(o2) : o1 === o2;
  }

  addHarvestTypeToCollectionIfMissing<Type extends Pick<IHarvestType, 'id'>>(
    harvestTypeCollection: Type[],
    ...harvestTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const harvestTypes: Type[] = harvestTypesToCheck.filter(isPresent);
    if (harvestTypes.length > 0) {
      const harvestTypeCollectionIdentifiers = harvestTypeCollection.map(harvestTypeItem => this.getHarvestTypeIdentifier(harvestTypeItem));
      const harvestTypesToAdd = harvestTypes.filter(harvestTypeItem => {
        const harvestTypeIdentifier = this.getHarvestTypeIdentifier(harvestTypeItem);
        if (harvestTypeCollectionIdentifiers.includes(harvestTypeIdentifier)) {
          return false;
        }
        harvestTypeCollectionIdentifiers.push(harvestTypeIdentifier);
        return true;
      });
      return [...harvestTypesToAdd, ...harvestTypeCollection];
    }
    return harvestTypeCollection;
  }

  protected convertDateFromClient<T extends IHarvestType | NewHarvestType | PartialUpdateHarvestType>(harvestType: T): RestOf<T> {
    return {
      ...harvestType,
      dateCreated: harvestType.dateCreated?.toJSON() ?? null,
      dateModified: harvestType.dateModified?.toJSON() ?? null,
      dateSynched: harvestType.dateSynched?.toJSON() ?? null,
      dateDeleted: harvestType.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHarvestType: RestHarvestType): IHarvestType {
    return {
      ...restHarvestType,
      dateCreated: restHarvestType.dateCreated ? dayjs(restHarvestType.dateCreated) : undefined,
      dateModified: restHarvestType.dateModified ? dayjs(restHarvestType.dateModified) : undefined,
      dateSynched: restHarvestType.dateSynched ? dayjs(restHarvestType.dateSynched) : undefined,
      dateDeleted: restHarvestType.dateDeleted ? dayjs(restHarvestType.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHarvestType>): HttpResponse<IHarvestType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHarvestType[]>): HttpResponse<IHarvestType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
