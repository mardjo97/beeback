import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedingHive } from '../feeding-hive.model';
import { FeedingHiveService } from '../service/feeding-hive.service';

const feedingHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeedingHive> => {
  const id = route.params.id;
  if (id) {
    return inject(FeedingHiveService)
      .find(id)
      .pipe(
        mergeMap((feedingHive: HttpResponse<IFeedingHive>) => {
          if (feedingHive.body) {
            return of(feedingHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default feedingHiveResolve;
