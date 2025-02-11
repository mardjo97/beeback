import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHiveLocation, NewHiveLocation } from '../hive-location.model';

export type PartialUpdateHiveLocation = Partial<IHiveLocation> & Pick<IHiveLocation, 'id'>;

type RestOf<T extends IHiveLocation | NewHiveLocation> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestHiveLocation = RestOf<IHiveLocation>;

export type NewRestHiveLocation = RestOf<NewHiveLocation>;

export type PartialUpdateRestHiveLocation = RestOf<PartialUpdateHiveLocation>;

export type EntityResponseType = HttpResponse<IHiveLocation>;
export type EntityArrayResponseType = HttpResponse<IHiveLocation[]>;

@Injectable({ providedIn: 'root' })
export class HiveLocationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hive-locations');

  create(hiveLocation: NewHiveLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveLocation);
    return this.http
      .post<RestHiveLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hiveLocation: IHiveLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveLocation);
    return this.http
      .put<RestHiveLocation>(`${this.resourceUrl}/${this.getHiveLocationIdentifier(hiveLocation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hiveLocation: PartialUpdateHiveLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveLocation);
    return this.http
      .patch<RestHiveLocation>(`${this.resourceUrl}/${this.getHiveLocationIdentifier(hiveLocation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHiveLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHiveLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHiveLocationIdentifier(hiveLocation: Pick<IHiveLocation, 'id'>): number {
    return hiveLocation.id;
  }

  compareHiveLocation(o1: Pick<IHiveLocation, 'id'> | null, o2: Pick<IHiveLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getHiveLocationIdentifier(o1) === this.getHiveLocationIdentifier(o2) : o1 === o2;
  }

  addHiveLocationToCollectionIfMissing<Type extends Pick<IHiveLocation, 'id'>>(
    hiveLocationCollection: Type[],
    ...hiveLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hiveLocations: Type[] = hiveLocationsToCheck.filter(isPresent);
    if (hiveLocations.length > 0) {
      const hiveLocationCollectionIdentifiers = hiveLocationCollection.map(hiveLocationItem =>
        this.getHiveLocationIdentifier(hiveLocationItem),
      );
      const hiveLocationsToAdd = hiveLocations.filter(hiveLocationItem => {
        const hiveLocationIdentifier = this.getHiveLocationIdentifier(hiveLocationItem);
        if (hiveLocationCollectionIdentifiers.includes(hiveLocationIdentifier)) {
          return false;
        }
        hiveLocationCollectionIdentifiers.push(hiveLocationIdentifier);
        return true;
      });
      return [...hiveLocationsToAdd, ...hiveLocationCollection];
    }
    return hiveLocationCollection;
  }

  protected convertDateFromClient<T extends IHiveLocation | NewHiveLocation | PartialUpdateHiveLocation>(hiveLocation: T): RestOf<T> {
    return {
      ...hiveLocation,
      dateCreated: hiveLocation.dateCreated?.toJSON() ?? null,
      dateModified: hiveLocation.dateModified?.toJSON() ?? null,
      dateSynched: hiveLocation.dateSynched?.toJSON() ?? null,
      dateDeleted: hiveLocation.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHiveLocation: RestHiveLocation): IHiveLocation {
    return {
      ...restHiveLocation,
      dateCreated: restHiveLocation.dateCreated ? dayjs(restHiveLocation.dateCreated) : undefined,
      dateModified: restHiveLocation.dateModified ? dayjs(restHiveLocation.dateModified) : undefined,
      dateSynched: restHiveLocation.dateSynched ? dayjs(restHiveLocation.dateSynched) : undefined,
      dateDeleted: restHiveLocation.dateDeleted ? dayjs(restHiveLocation.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHiveLocation>): HttpResponse<IHiveLocation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHiveLocation[]>): HttpResponse<IHiveLocation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
