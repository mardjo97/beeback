import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHiveType, NewHiveType } from '../hive-type.model';

export type PartialUpdateHiveType = Partial<IHiveType> & Pick<IHiveType, 'id'>;

type RestOf<T extends IHiveType | NewHiveType> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestHiveType = RestOf<IHiveType>;

export type NewRestHiveType = RestOf<NewHiveType>;

export type PartialUpdateRestHiveType = RestOf<PartialUpdateHiveType>;

export type EntityResponseType = HttpResponse<IHiveType>;
export type EntityArrayResponseType = HttpResponse<IHiveType[]>;

@Injectable({ providedIn: 'root' })
export class HiveTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hive-types');

  create(hiveType: NewHiveType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveType);
    return this.http
      .post<RestHiveType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hiveType: IHiveType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveType);
    return this.http
      .put<RestHiveType>(`${this.resourceUrl}/${this.getHiveTypeIdentifier(hiveType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hiveType: PartialUpdateHiveType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hiveType);
    return this.http
      .patch<RestHiveType>(`${this.resourceUrl}/${this.getHiveTypeIdentifier(hiveType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHiveType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHiveType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHiveTypeIdentifier(hiveType: Pick<IHiveType, 'id'>): number {
    return hiveType.id;
  }

  compareHiveType(o1: Pick<IHiveType, 'id'> | null, o2: Pick<IHiveType, 'id'> | null): boolean {
    return o1 && o2 ? this.getHiveTypeIdentifier(o1) === this.getHiveTypeIdentifier(o2) : o1 === o2;
  }

  addHiveTypeToCollectionIfMissing<Type extends Pick<IHiveType, 'id'>>(
    hiveTypeCollection: Type[],
    ...hiveTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hiveTypes: Type[] = hiveTypesToCheck.filter(isPresent);
    if (hiveTypes.length > 0) {
      const hiveTypeCollectionIdentifiers = hiveTypeCollection.map(hiveTypeItem => this.getHiveTypeIdentifier(hiveTypeItem));
      const hiveTypesToAdd = hiveTypes.filter(hiveTypeItem => {
        const hiveTypeIdentifier = this.getHiveTypeIdentifier(hiveTypeItem);
        if (hiveTypeCollectionIdentifiers.includes(hiveTypeIdentifier)) {
          return false;
        }
        hiveTypeCollectionIdentifiers.push(hiveTypeIdentifier);
        return true;
      });
      return [...hiveTypesToAdd, ...hiveTypeCollection];
    }
    return hiveTypeCollection;
  }

  protected convertDateFromClient<T extends IHiveType | NewHiveType | PartialUpdateHiveType>(hiveType: T): RestOf<T> {
    return {
      ...hiveType,
      dateCreated: hiveType.dateCreated?.toJSON() ?? null,
      dateModified: hiveType.dateModified?.toJSON() ?? null,
      dateSynched: hiveType.dateSynched?.toJSON() ?? null,
      dateDeleted: hiveType.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHiveType: RestHiveType): IHiveType {
    return {
      ...restHiveType,
      dateCreated: restHiveType.dateCreated ? dayjs(restHiveType.dateCreated) : undefined,
      dateModified: restHiveType.dateModified ? dayjs(restHiveType.dateModified) : undefined,
      dateSynched: restHiveType.dateSynched ? dayjs(restHiveType.dateSynched) : undefined,
      dateDeleted: restHiveType.dateDeleted ? dayjs(restHiveType.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHiveType>): HttpResponse<IHiveType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHiveType[]>): HttpResponse<IHiveType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
