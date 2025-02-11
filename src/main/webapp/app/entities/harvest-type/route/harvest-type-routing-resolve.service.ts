import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHarvestType } from '../harvest-type.model';
import { HarvestTypeService } from '../service/harvest-type.service';

const harvestTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IHarvestType> => {
  const id = route.params.id;
  if (id) {
    return inject(HarvestTypeService)
      .find(id)
      .pipe(
        mergeMap((harvestType: HttpResponse<IHarvestType>) => {
          if (harvestType.body) {
            return of(harvestType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default harvestTypeResolve;
