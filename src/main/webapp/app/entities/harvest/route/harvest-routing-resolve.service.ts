import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHarvest } from '../harvest.model';
import { HarvestService } from '../service/harvest.service';

const harvestResolve = (route: ActivatedRouteSnapshot): Observable<null | IHarvest> => {
  const id = route.params.id;
  if (id) {
    return inject(HarvestService)
      .find(id)
      .pipe(
        mergeMap((harvest: HttpResponse<IHarvest>) => {
          if (harvest.body) {
            return of(harvest.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default harvestResolve;
