import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExaminationHive } from '../examination-hive.model';
import { ExaminationHiveService } from '../service/examination-hive.service';

const examinationHiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IExaminationHive> => {
  const id = route.params.id;
  if (id) {
    return inject(ExaminationHiveService)
      .find(id)
      .pipe(
        mergeMap((examinationHive: HttpResponse<IExaminationHive>) => {
          if (examinationHive.body) {
            return of(examinationHive.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default examinationHiveResolve;
