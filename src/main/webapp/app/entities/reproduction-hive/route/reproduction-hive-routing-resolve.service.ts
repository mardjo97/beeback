import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReproductionHive } from '../reproduction-hive.model';
import { ReproductionHiveService } from '../service/reproduction-hive.service';

const reproductionHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IReproductionHive> => {
  const id = route.params.id;
  if (id) {
    return inject(ReproductionHiveService)
      .find(id)
      .pipe(
        mergeMap((reproductionHive: HttpResponse<IReproductionHive>) => {
          if (reproductionHive.body) {
            return of(reproductionHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reproductionHiveResolve;
