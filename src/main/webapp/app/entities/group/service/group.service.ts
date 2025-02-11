import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGroup, NewGroup } from '../group.model';

export type PartialUpdateGroup = Partial<IGroup> & Pick<IGroup, 'id'>;

type RestOf<T extends IGroup | NewGroup> = Omit<T, 'dateCreated' | 'dateModified' | 'dateSynched' | 'dateDeleted'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
  dateSynched?: string | null;
  dateDeleted?: string | null;
};

export type RestGroup = RestOf<IGroup>;

export type NewRestGroup = RestOf<NewGroup>;

export type PartialUpdateRestGroup = RestOf<PartialUpdateGroup>;

export type EntityResponseType = HttpResponse<IGroup>;
export type EntityArrayResponseType = HttpResponse<IGroup[]>;

@Injectable({ providedIn: 'root' })
export class GroupService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/groups');

  create(group: NewGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(group);
    return this.http.post<RestGroup>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(group: IGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(group);
    return this.http
      .put<RestGroup>(`${this.resourceUrl}/${this.getGroupIdentifier(group)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(group: PartialUpdateGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(group);
    return this.http
      .patch<RestGroup>(`${this.resourceUrl}/${this.getGroupIdentifier(group)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGroupIdentifier(group: Pick<IGroup, 'id'>): number {
    return group.id;
  }

  compareGroup(o1: Pick<IGroup, 'id'> | null, o2: Pick<IGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getGroupIdentifier(o1) === this.getGroupIdentifier(o2) : o1 === o2;
  }

  addGroupToCollectionIfMissing<Type extends Pick<IGroup, 'id'>>(
    groupCollection: Type[],
    ...groupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const groups: Type[] = groupsToCheck.filter(isPresent);
    if (groups.length > 0) {
      const groupCollectionIdentifiers = groupCollection.map(groupItem => this.getGroupIdentifier(groupItem));
      const groupsToAdd = groups.filter(groupItem => {
        const groupIdentifier = this.getGroupIdentifier(groupItem);
        if (groupCollectionIdentifiers.includes(groupIdentifier)) {
          return false;
        }
        groupCollectionIdentifiers.push(groupIdentifier);
        return true;
      });
      return [...groupsToAdd, ...groupCollection];
    }
    return groupCollection;
  }

  protected convertDateFromClient<T extends IGroup | NewGroup | PartialUpdateGroup>(group: T): RestOf<T> {
    return {
      ...group,
      dateCreated: group.dateCreated?.toJSON() ?? null,
      dateModified: group.dateModified?.toJSON() ?? null,
      dateSynched: group.dateSynched?.toJSON() ?? null,
      dateDeleted: group.dateDeleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restGroup: RestGroup): IGroup {
    return {
      ...restGroup,
      dateCreated: restGroup.dateCreated ? dayjs(restGroup.dateCreated) : undefined,
      dateModified: restGroup.dateModified ? dayjs(restGroup.dateModified) : undefined,
      dateSynched: restGroup.dateSynched ? dayjs(restGroup.dateSynched) : undefined,
      dateDeleted: restGroup.dateDeleted ? dayjs(restGroup.dateDeleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGroup>): HttpResponse<IGroup> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGroup[]>): HttpResponse<IGroup[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
