import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMovedHive } from '../moved-hive.model';
import { MovedHiveService } from '../service/moved-hive.service';

const movedHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IMovedHive> => {
  const id = route.params.id;
  if (id) {
    return inject(MovedHiveService)
      .find(id)
      .pipe(
        mergeMap((movedHive: HttpResponse<IMovedHive>) => {
          if (movedHive.body) {
            return of(movedHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default movedHiveResolve;
