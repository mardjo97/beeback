import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHarvest, NewHarvest } from '../harvest.model';

export type PartialUpdateHarvest = Partial<IHarvest> & Pick<IHarvest, 'id'>;

type RestOf<T extends IHarvest | NewHarvest> = Omit<T, 'dateCollected' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCollected?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestHarvest = RestOf<IHarvest>;

export type NewRestHarvest = RestOf<NewHarvest>;

export type PartialUpdateRestHarvest = RestOf<PartialUpdateHarvest>;

export type EntityResponseType = HttpResponse<IHarvest>;
export type EntityArrayResponseType = HttpResponse<IHarvest[]>;

@Injectable({ providedIn: 'root' })
export class HarvestService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/harvests');

  create(harvest: NewHarvest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvest);
    return this.http
      .post<RestHarvest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(harvest: IHarvest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvest);
    return this.http
      .put<RestHarvest>(`${this.resourceUrl}/${this.getHarvestIdentifier(harvest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(harvest: PartialUpdateHarvest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(harvest);
    return this.http
      .patch<RestHarvest>(`${this.resourceUrl}/${this.getHarvestIdentifier(harvest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHarvest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHarvest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHarvestIdentifier(harvest: Pick<IHarvest, 'id'>): number {
    return harvest.id;
  }

  compareHarvest(o1: Pick<IHarvest, 'id'> | null, o2: Pick<IHarvest, 'id'> | null): boolean {
    return o1 && o2 ? this.getHarvestIdentifier(o1) === this.getHarvestIdentifier(o2) : o1 === o2;
  }

  addHarvestToCollectionIfMissing<Type extends Pick<IHarvest, 'id'>>(
    harvestCollection: Type[],
    ...harvestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const harvests: Type[] = harvestsToCheck.filter(isPresent);
    if (harvests.length > 0) {
      const harvestCollectionIdentifiers = harvestCollection.map(harvestItem => this.getHarvestIdentifier(harvestItem));
      const harvestsToAdd = harvests.filter(harvestItem => {
        const harvestIdentifier = this.getHarvestIdentifier(harvestItem);
        if (harvestCollectionIdentifiers.includes(harvestIdentifier)) {
          return false;
        }
        harvestCollectionIdentifiers.push(harvestIdentifier);
        return true;
      });
      return [...harvestsToAdd, ...harvestCollection];
    }
    return harvestCollection;
  }

  protected convertDateFromClient<T extends IHarvest | NewHarvest | PartialUpdateHarvest>(harvest: T): RestOf<T> {
    return {
      ...harvest,
      dateCollected: harvest.dateCollected?.toJSON() ?? null,
      dateCreated: harvest.dateCreated?.toJSON() ?? null,
      dateModified: harvest.dateModified?.toJSON() ?? null,
      dateSynched: harvest.dateSynched?.toJSON() ?? null,
      dateDeleted: harvest.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHarvest: RestHarvest): IHarvest {
    return {
      ...restHarvest,
      dateCollected: restHarvest.dateCollected ? dayjs(restHarvest.dateCollected) : undefined,
      dateCreated: restHarvest.dateCreated ? dayjs(restHarvest.dateCreated) : undefined,
      dateModified: restHarvest.dateModified ? dayjs(restHarvest.dateModified) : undefined,
      dateSynched: restHarvest.dateSynched ? dayjs(restHarvest.dateSynched) : undefined,
      dateDeleted: restHarvest.dateDeleted ? dayjs(restHarvest.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHarvest>): HttpResponse<IHarvest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHarvest[]>): HttpResponse<IHarvest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
