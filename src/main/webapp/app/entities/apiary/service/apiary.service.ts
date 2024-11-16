import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApiary, NewApiary } from '../apiary.model';

export type PartialUpdateApiary = Partial<IApiary> & Pick<IApiary, 'id'>;

type RestOf<T extends IApiary | NewApiary> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
};

export type RestApiary = RestOf<IApiary>;

export type NewRestApiary = RestOf<NewApiary>;

export type PartialUpdateRestApiary = RestOf<PartialUpdateApiary>;

export type EntityResponseType = HttpResponse<IApiary>;
export type EntityArrayResponseType = HttpResponse<IApiary[]>;

@Injectable({ providedIn: 'root' })
export class ApiaryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apiaries');

  create(apiary: NewApiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apiary);
    return this.http
      .post<RestApiary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(apiary: IApiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apiary);
    return this.http
      .put<RestApiary>(`${this.resourceUrl}/${this.getApiaryIdentifier(apiary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(apiary: PartialUpdateApiary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apiary);
    return this.http
      .patch<RestApiary>(`${this.resourceUrl}/${this.getApiaryIdentifier(apiary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestApiary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestApiary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApiaryIdentifier(apiary: Pick<IApiary, 'id'>): number {
    return apiary.id;
  }

  compareApiary(o1: Pick<IApiary, 'id'> | null, o2: Pick<IApiary, 'id'> | null): boolean {
    return o1 && o2 ? this.getApiaryIdentifier(o1) === this.getApiaryIdentifier(o2) : o1 === o2;
  }

  addApiaryToCollectionIfMissing<Type extends Pick<IApiary, 'id'>>(
    apiaryCollection: Type[],
    ...apiariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apiaries: Type[] = apiariesToCheck.filter(isPresent);
    if (apiaries.length > 0) {
      const apiaryCollectionIdentifiers = apiaryCollection.map(apiaryItem => this.getApiaryIdentifier(apiaryItem));
      const apiariesToAdd = apiaries.filter(apiaryItem => {
        const apiaryIdentifier = this.getApiaryIdentifier(apiaryItem);
        if (apiaryCollectionIdentifiers.includes(apiaryIdentifier)) {
          return false;
        }
        apiaryCollectionIdentifiers.push(apiaryIdentifier);
        return true;
      });
      return [...apiariesToAdd, ...apiaryCollection];
    }
    return apiaryCollection;
  }

  protected convertDateFromClient<T extends IApiary | NewApiary | PartialUpdateApiary>(apiary: T): RestOf<T> {
    return {
      ...apiary,
      dateCreated: apiary.dateCreated?.toJSON() ?? null,
      dateModified: apiary.dateModified?.toJSON() ?? null,
      dateSynched: apiary.dateSynched?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restApiary: RestApiary): IApiary {
    return {
      ...restApiary,
      dateCreated: restApiary.dateCreated ? dayjs(restApiary.dateCreated) : undefined,
      dateModified: restApiary.dateModified ? dayjs(restApiary.dateModified) : undefined,
      dateSynched: restApiary.dateSynched ? dayjs(restApiary.dateSynched) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestApiary>): HttpResponse<IApiary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestApiary[]>): HttpResponse<IApiary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
