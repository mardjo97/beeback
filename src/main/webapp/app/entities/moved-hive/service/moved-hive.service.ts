import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMovedHive, NewMovedHive } from '../moved-hive.model';

export type PartialUpdateMovedHive = Partial<IMovedHive> & Pick<IMovedHive, 'id'>;

type RestOf<T extends IMovedHive | NewMovedHive> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestMovedHive = RestOf<IMovedHive>;

export type NewRestMovedHive = RestOf<NewMovedHive>;

export type PartialUpdateRestMovedHive = RestOf<PartialUpdateMovedHive>;

export type EntityResponseType = HttpResponse<IMovedHive>;
export type EntityArrayResponseType = HttpResponse<IMovedHive[]>;

@Injectable({ providedIn: 'root' })
export class MovedHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/moved-hives');

  create(movedHive: NewMovedHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movedHive);
    return this.http
      .post<RestMovedHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(movedHive: IMovedHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movedHive);
    return this.http
      .put<RestMovedHive>(`${this.resourceUrl}/${this.getMovedHiveIdentifier(movedHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(movedHive: PartialUpdateMovedHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movedHive);
    return this.http
      .patch<RestMovedHive>(`${this.resourceUrl}/${this.getMovedHiveIdentifier(movedHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMovedHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMovedHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMovedHiveIdentifier(movedHive: Pick<IMovedHive, 'id'>): number {
    return movedHive.id;
  }

  compareMovedHive(o1: Pick<IMovedHive, 'id'> | null, o2: Pick<IMovedHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getMovedHiveIdentifier(o1) === this.getMovedHiveIdentifier(o2) : o1 === o2;
  }

  addMovedHiveToCollectionIfMissing<Type extends Pick<IMovedHive, 'id'>>(
    movedHiveCollection: Type[],
    ...movedHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const movedHives: Type[] = movedHivesToCheck.filter(isPresent);
    if (movedHives.length > 0) {
      const movedHiveCollectionIdentifiers = movedHiveCollection.map(movedHiveItem => this.getMovedHiveIdentifier(movedHiveItem));
      const movedHivesToAdd = movedHives.filter(movedHiveItem => {
        const movedHiveIdentifier = this.getMovedHiveIdentifier(movedHiveItem);
        if (movedHiveCollectionIdentifiers.includes(movedHiveIdentifier)) {
          return false;
        }
        movedHiveCollectionIdentifiers.push(movedHiveIdentifier);
        return true;
      });
      return [...movedHivesToAdd, ...movedHiveCollection];
    }
    return movedHiveCollection;
  }

  protected convertDateFromClient<T extends IMovedHive | NewMovedHive | PartialUpdateMovedHive>(movedHive: T): RestOf<T> {
    return {
      ...movedHive,
      dateCreated: movedHive.dateCreated?.toJSON() ?? null,
      dateModified: movedHive.dateModified?.toJSON() ?? null,
      dateSynched: movedHive.dateSynched?.toJSON() ?? null,
      dateDeleted: movedHive.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMovedHive: RestMovedHive): IMovedHive {
    return {
      ...restMovedHive,
      dateCreated: restMovedHive.dateCreated ? dayjs(restMovedHive.dateCreated) : undefined,
      dateModified: restMovedHive.dateModified ? dayjs(restMovedHive.dateModified) : undefined,
      dateSynched: restMovedHive.dateSynched ? dayjs(restMovedHive.dateSynched) : undefined,
      dateDeleted: restMovedHive.dateDeleted ? dayjs(restMovedHive.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMovedHive>): HttpResponse<IMovedHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMovedHive[]>): HttpResponse<IMovedHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
