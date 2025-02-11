import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHiveLocation } from '../hive-location.model';
import { HiveLocationService } from '../service/hive-location.service';

const hiveLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IHiveLocation> => {
  const id = route.params.id;
  if (id) {
    return inject(HiveLocationService)
      .find(id)
      .pipe(
        mergeMap((hiveLocation: HttpResponse<IHiveLocation>) => {
          if (hiveLocation.body) {
            return of(hiveLocation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default hiveLocationResolve;
