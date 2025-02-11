import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQueen } from '../queen.model';
import { QueenService } from '../service/queen.service';

const queenResolve = (route: ActivatedRouteSnapshot): Observable<null | IQueen> => {
  const id = route.params.id;
  if (id) {
    return inject(QueenService)
      .find(id)
      .pipe(
        mergeMap((queen: HttpResponse<IQueen>) => {
          if (queen.body) {
            return of(queen.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default queenResolve;
