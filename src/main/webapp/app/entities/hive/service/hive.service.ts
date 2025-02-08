import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHive, NewHive } from '../hive.model';

export type PartialUpdateHive = Partial<IHive> & Pick<IHive, 'id'>;

type RestOf<T extends IHive | NewHive> = Omit<
  T,
  'examinationDate' | 'archivedDate' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'
> & {
  examinationDate?: string | null;
  archivedDate?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestHive = RestOf<IHive>;

export type NewRestHive = RestOf<NewHive>;

export type PartialUpdateRestHive = RestOf<PartialUpdateHive>;

export type EntityResponseType = HttpResponse<IHive>;
export type EntityArrayResponseType = HttpResponse<IHive[]>;

@Injectable({ providedIn: 'root' })
export class HiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hives');

  create(hive: NewHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hive);
    return this.http.post<RestHive>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hive: IHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hive);
    return this.http
      .put<RestHive>(`${this.resourceUrl}/${this.getHiveIdentifier(hive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hive: PartialUpdateHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hive);
    return this.http
      .patch<RestHive>(`${this.resourceUrl}/${this.getHiveIdentifier(hive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHiveIdentifier(hive: Pick<IHive, 'id'>): number {
    return hive.id;
  }

  compareHive(o1: Pick<IHive, 'id'> | null, o2: Pick<IHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getHiveIdentifier(o1) === this.getHiveIdentifier(o2) : o1 === o2;
  }

  addHiveToCollectionIfMissing<Type extends Pick<IHive, 'id'>>(
    hiveCollection: Type[],
    ...hivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hives: Type[] = hivesToCheck.filter(isPresent);
    if (hives.length > 0) {
      const hiveCollectionIdentifiers = hiveCollection.map(hiveItem => this.getHiveIdentifier(hiveItem));
      const hivesToAdd = hives.filter(hiveItem => {
        const hiveIdentifier = this.getHiveIdentifier(hiveItem);
        if (hiveCollectionIdentifiers.includes(hiveIdentifier)) {
          return false;
        }
        hiveCollectionIdentifiers.push(hiveIdentifier);
        return true;
      });
      return [...hivesToAdd, ...hiveCollection];
    }
    return hiveCollection;
  }

  protected convertDateFromClient<T extends IHive | NewHive | PartialUpdateHive>(hive: T): RestOf<T> {
    return {
      ...hive,
      examinationDate: hive.examinationDate?.toJSON() ?? null,
      archivedDate: hive.archivedDate?.toJSON() ?? null,
      dateCreated: hive.dateCreated?.toJSON() ?? null,
      dateModified: hive.dateModified?.toJSON() ?? null,
      dateSynched: hive.dateSynched?.toJSON() ?? null,
      dateDeleted: hive.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHive: RestHive): IHive {
    return {
      ...restHive,
      examinationDate: restHive.examinationDate ? dayjs(restHive.examinationDate) : undefined,
      archivedDate: restHive.archivedDate ? dayjs(restHive.archivedDate) : undefined,
      dateCreated: restHive.dateCreated ? dayjs(restHive.dateCreated) : undefined,
      dateModified: restHive.dateModified ? dayjs(restHive.dateModified) : undefined,
      dateSynched: restHive.dateSynched ? dayjs(restHive.dateSynched) : undefined,
      dateDeleted: restHive.dateDeleted ? dayjs(restHive.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHive>): HttpResponse<IHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHive[]>): HttpResponse<IHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
