import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { DeleteRequest } from './delete.model';

@Injectable({ providedIn: 'root' })
export class DeleteService {
  private readonly http = inject(HttpClient);
  private readonly applicationConfigService = inject(ApplicationConfigService);

  save(deleteRequest: DeleteRequest): Observable<{}> {
    return this.http.post(this.applicationConfigService.getEndpointFor('api/delete'), deleteRequest);
  }
}
