import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHiveType } from '../hive-type.model';
import { HiveTypeService } from '../service/hive-type.service';

const hiveTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IHiveType> => {
  const id = route.params.id;
  if (id) {
    return inject(HiveTypeService)
      .find(id)
      .pipe(
        mergeMap((hiveType: HttpResponse<IHiveType>) => {
          if (hiveType.body) {
            return of(hiveType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default hiveTypeResolve;
