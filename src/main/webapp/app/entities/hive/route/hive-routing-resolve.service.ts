import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHive } from '../hive.model';
import { HiveService } from '../service/hive.service';

const hiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IHive> => {
  const id = route.params.id;
  if (id) {
    return inject(HiveService)
      .find(id)
      .pipe(
        mergeMap((hive: HttpResponse<IHive>) => {
          if (hive.body) {
            return of(hive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default hiveResolve;
