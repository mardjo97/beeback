import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQueenChangeHive } from '../queen-change-hive.model';
import { QueenChangeHiveService } from '../service/queen-change-hive.service';

const queenChangeHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IQueenChangeHive> => {
  const id = route.params.id;
  if (id) {
    return inject(QueenChangeHiveService)
      .find(id)
      .pipe(
        mergeMap((queenChangeHive: HttpResponse<IQueenChangeHive>) => {
          if (queenChangeHive.body) {
            return of(queenChangeHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default queenChangeHiveResolve;
