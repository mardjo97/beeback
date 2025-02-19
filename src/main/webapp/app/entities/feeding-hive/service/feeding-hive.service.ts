import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedingHive, NewFeedingHive } from '../feeding-hive.model';

export type PartialUpdateFeedingHive = Partial<IFeedingHive> & Pick<IFeedingHive, 'id'>;

type RestOf<T extends IFeedingHive | NewFeedingHive> = Omit<
  T,
  'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
  dateFinished?: string | null;
};

export type RestFeedingHive = RestOf<IFeedingHive>;

export type NewRestFeedingHive = RestOf<NewFeedingHive>;

export type PartialUpdateRestFeedingHive = RestOf<PartialUpdateFeedingHive>;

export type EntityResponseType = HttpResponse<IFeedingHive>;
export type EntityArrayResponseType = HttpResponse<IFeedingHive[]>;

@Injectable({ providedIn: 'root' })
export class FeedingHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feeding-hives');

  create(feedingHive: NewFeedingHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedingHive);
    return this.http
      .post<RestFeedingHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(feedingHive: IFeedingHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedingHive);
    return this.http
      .put<RestFeedingHive>(`${this.resourceUrl}/${this.getFeedingHiveIdentifier(feedingHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(feedingHive: PartialUpdateFeedingHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedingHive);
    return this.http
      .patch<RestFeedingHive>(`${this.resourceUrl}/${this.getFeedingHiveIdentifier(feedingHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeedingHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeedingHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedingHiveIdentifier(feedingHive: Pick<IFeedingHive, 'id'>): number {
    return feedingHive.id;
  }

  compareFeedingHive(o1: Pick<IFeedingHive, 'id'> | null, o2: Pick<IFeedingHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedingHiveIdentifier(o1) === this.getFeedingHiveIdentifier(o2) : o1 === o2;
  }

  addFeedingHiveToCollectionIfMissing<Type extends Pick<IFeedingHive, 'id'>>(
    feedingHiveCollection: Type[],
    ...feedingHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedingHives: Type[] = feedingHivesToCheck.filter(isPresent);
    if (feedingHives.length > 0) {
      const feedingHiveCollectionIdentifiers = feedingHiveCollection.map(feedingHiveItem => this.getFeedingHiveIdentifier(feedingHiveItem));
      const feedingHivesToAdd = feedingHives.filter(feedingHiveItem => {
        const feedingHiveIdentifier = this.getFeedingHiveIdentifier(feedingHiveItem);
        if (feedingHiveCollectionIdentifiers.includes(feedingHiveIdentifier)) {
          return false;
        }
        feedingHiveCollectionIdentifiers.push(feedingHiveIdentifier);
        return true;
      });
      return [...feedingHivesToAdd, ...feedingHiveCollection];
    }
    return feedingHiveCollection;
  }

  protected convertDateFromClient<T extends IFeedingHive | NewFeedingHive | PartialUpdateFeedingHive>(feedingHive: T): RestOf<T> {
    return {
      ...feedingHive,
      dateCreated: feedingHive.dateCreated?.toJSON() ?? null,
      dateModified: feedingHive.dateModified?.toJSON() ?? null,
      dateSynched: feedingHive.dateSynched?.toJSON() ?? null,
      dateDeleted: feedingHive.dateDeleted?.toJSON() ?? null,
      dateFinished: feedingHive.dateFinished?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFeedingHive: RestFeedingHive): IFeedingHive {
    return {
      ...restFeedingHive,
      dateCreated: restFeedingHive.dateCreated ? dayjs(restFeedingHive.dateCreated) : undefined,
      dateModified: restFeedingHive.dateModified ? dayjs(restFeedingHive.dateModified) : undefined,
      dateSynched: restFeedingHive.dateSynched ? dayjs(restFeedingHive.dateSynched) : undefined,
      dateDeleted: restFeedingHive.dateDeleted ? dayjs(restFeedingHive.dateDeleted) : undefined,
      dateFinished: restFeedingHive.dateFinished ? dayjs(restFeedingHive.dateFinished) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeedingHive>): HttpResponse<IFeedingHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeedingHive[]>): HttpResponse<IFeedingHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
