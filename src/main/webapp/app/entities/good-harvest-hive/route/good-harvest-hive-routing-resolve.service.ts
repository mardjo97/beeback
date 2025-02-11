import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGoodHarvestHive } from '../good-harvest-hive.model';
import { GoodHarvestHiveService } from '../service/good-harvest-hive.service';

const goodHarvestHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IGoodHarvestHive> => {
  const id = route.params.id;
  if (id) {
    return inject(GoodHarvestHiveService)
      .find(id)
      .pipe(
        mergeMap((goodHarvestHive: HttpResponse<IGoodHarvestHive>) => {
          if (goodHarvestHive.body) {
            return of(goodHarvestHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default goodHarvestHiveResolve;
