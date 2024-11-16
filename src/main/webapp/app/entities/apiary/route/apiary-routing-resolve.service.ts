import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApiary } from '../apiary.model';
import { ApiaryService } from '../service/apiary.service';

const apiaryResolve = (route: ActivatedRouteSnapshot): Observable<null | IApiary> => {
  const id = route.params.id;
  if (id) {
    return inject(ApiaryService)
      .find(id)
      .pipe(
        mergeMap((apiary: HttpResponse<IApiary>) => {
          if (apiary.body) {
            return of(apiary.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default apiaryResolve;
