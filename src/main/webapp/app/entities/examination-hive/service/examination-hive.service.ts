import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExaminationHive, NewExaminationHive } from '../examination-hive.model';

export type PartialUpdateExaminationHive = Partial<IExaminationHive> & Pick<IExaminationHive, 'id'>;

type RestOf<T extends IExaminationHive | NewExaminationHive> = Omit<
  T,
  'dateExamination' | 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted' | 'dateFinished'
> & {
  dateExamination?: string | null;
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
  dateFinished?: string | null;
};

export type RestExaminationHive = RestOf<IExaminationHive>;

export type NewRestExaminationHive = RestOf<NewExaminationHive>;

export type PartialUpdateRestExaminationHive = RestOf<PartialUpdateExaminationHive>;

export type EntityResponseType = HttpResponse<IExaminationHive>;
export type EntityArrayResponseType = HttpResponse<IExaminationHive[]>;

@Injectable({ providedIn: 'root' })
export class ExaminationHiveService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/examination-hives');

  create(examinationHive: NewExaminationHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examinationHive);
    return this.http
      .post<RestExaminationHive>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(examinationHive: IExaminationHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examinationHive);
    return this.http
      .put<RestExaminationHive>(`${this.resourceUrl}/${this.getExaminationHiveIdentifier(examinationHive)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(examinationHive: PartialUpdateExaminationHive): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examinationHive);
    return this.http
      .patch<RestExaminationHive>(`${this.resourceUrl}/${this.getExaminationHiveIdentifier(examinationHive)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExaminationHive>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExaminationHive[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExaminationHiveIdentifier(examinationHive: Pick<IExaminationHive, 'id'>): number {
    return examinationHive.id;
  }

  compareExaminationHive(o1: Pick<IExaminationHive, 'id'> | null, o2: Pick<IExaminationHive, 'id'> | null): boolean {
    return o1 && o2 ? this.getExaminationHiveIdentifier(o1) === this.getExaminationHiveIdentifier(o2) : o1 === o2;
  }

  addExaminationHiveToCollectionIfMissing<Type extends Pick<IExaminationHive, 'id'>>(
    examinationHiveCollection: Type[],
    ...examinationHivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const examinationHives: Type[] = examinationHivesToCheck.filter(isPresent);
    if (examinationHives.length > 0) {
      const examinationHiveCollectionIdentifiers = examinationHiveCollection.map(examinationHiveItem =>
        this.getExaminationHiveIdentifier(examinationHiveItem),
      );
      const examinationHivesToAdd = examinationHives.filter(examinationHiveItem => {
        const examinationHiveIdentifier = this.getExaminationHiveIdentifier(examinationHiveItem);
        if (examinationHiveCollectionIdentifiers.includes(examinationHiveIdentifier)) {
          return false;
        }
        examinationHiveCollectionIdentifiers.push(examinationHiveIdentifier);
        return true;
      });
      return [...examinationHivesToAdd, ...examinationHiveCollection];
    }
    return examinationHiveCollection;
  }

  protected convertDateFromClient<T extends IExaminationHive | NewExaminationHive | PartialUpdateExaminationHive>(
    examinationHive: T,
  ): RestOf<T> {
    return {
      ...examinationHive,
      dateExamination: examinationHive.dateExamination?.toJSON() ?? null,
      dateCreated: examinationHive.dateCreated?.toJSON() ?? null,
      dateModified: examinationHive.dateModified?.toJSON() ?? null,
      dateSynched: examinationHive.dateSynched?.toJSON() ?? null,
      dateDeleted: examinationHive.dateDeleted?.toJSON() ?? null,
      dateFinished: examinationHive.dateFinished?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExaminationHive: RestExaminationHive): IExaminationHive {
    return {
      ...restExaminationHive,
      dateExamination: restExaminationHive.dateExamination ? dayjs(restExaminationHive.dateExamination) : undefined,
      dateCreated: restExaminationHive.dateCreated ? dayjs(restExaminationHive.dateCreated) : undefined,
      dateModified: restExaminationHive.dateModified ? dayjs(restExaminationHive.dateModified) : undefined,
      dateSynched: restExaminationHive.dateSynched ? dayjs(restExaminationHive.dateSynched) : undefined,
      dateDeleted: restExaminationHive.dateDeleted ? dayjs(restExaminationHive.dateDeleted) : undefined,
      dateFinished: restExaminationHive.dateFinished ? dayjs(restExaminationHive.dateFinished) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExaminationHive>): HttpResponse<IExaminationHive> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExaminationHive[]>): HttpResponse<IExaminationHive[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
