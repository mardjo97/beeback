import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppConfig, NewAppConfig } from '../app-config.model';

export type PartialUpdateAppConfig = Partial<IAppConfig> & Pick<IAppConfig, 'id'>;

type RestOf<T extends IAppConfig | NewAppConfig> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestAppConfig = RestOf<IAppConfig>;

export type NewRestAppConfig = RestOf<NewAppConfig>;

export type PartialUpdateRestAppConfig = RestOf<PartialUpdateAppConfig>;

export type EntityResponseType = HttpResponse<IAppConfig>;
export type EntityArrayResponseType = HttpResponse<IAppConfig[]>;

@Injectable({ providedIn: 'root' })
export class AppConfigService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-configs');

  create(appConfig: NewAppConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appConfig);
    return this.http
      .post<RestAppConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(appConfig: IAppConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appConfig);
    return this.http
      .put<RestAppConfig>(`${this.resourceUrl}/${this.getAppConfigIdentifier(appConfig)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(appConfig: PartialUpdateAppConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appConfig);
    return this.http
      .patch<RestAppConfig>(`${this.resourceUrl}/${this.getAppConfigIdentifier(appConfig)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAppConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAppConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppConfigIdentifier(appConfig: Pick<IAppConfig, 'id'>): number {
    return appConfig.id;
  }

  compareAppConfig(o1: Pick<IAppConfig, 'id'> | null, o2: Pick<IAppConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppConfigIdentifier(o1) === this.getAppConfigIdentifier(o2) : o1 === o2;
  }

  addAppConfigToCollectionIfMissing<Type extends Pick<IAppConfig, 'id'>>(
    appConfigCollection: Type[],
    ...appConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appConfigs: Type[] = appConfigsToCheck.filter(isPresent);
    if (appConfigs.length > 0) {
      const appConfigCollectionIdentifiers = appConfigCollection.map(appConfigItem => this.getAppConfigIdentifier(appConfigItem));
      const appConfigsToAdd = appConfigs.filter(appConfigItem => {
        const appConfigIdentifier = this.getAppConfigIdentifier(appConfigItem);
        if (appConfigCollectionIdentifiers.includes(appConfigIdentifier)) {
          return false;
        }
        appConfigCollectionIdentifiers.push(appConfigIdentifier);
        return true;
      });
      return [...appConfigsToAdd, ...appConfigCollection];
    }
    return appConfigCollection;
  }

  protected convertDateFromClient<T extends IAppConfig | NewAppConfig | PartialUpdateAppConfig>(appConfig: T): RestOf<T> {
    return {
      ...appConfig,
      dateCreated: appConfig.dateCreated?.toJSON() ?? null,
      dateModified: appConfig.dateModified?.toJSON() ?? null,
      dateSynched: appConfig.dateSynched?.toJSON() ?? null,
      dateDeleted: appConfig.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAppConfig: RestAppConfig): IAppConfig {
    return {
      ...restAppConfig,
      dateCreated: restAppConfig.dateCreated ? dayjs(restAppConfig.dateCreated) : undefined,
      dateModified: restAppConfig.dateModified ? dayjs(restAppConfig.dateModified) : undefined,
      dateSynched: restAppConfig.dateSynched ? dayjs(restAppConfig.dateSynched) : undefined,
      dateDeleted: restAppConfig.dateDeleted ? dayjs(restAppConfig.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAppConfig>): HttpResponse<IAppConfig> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAppConfig[]>): HttpResponse<IAppConfig[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
