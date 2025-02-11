import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IHiveLocation } from '../hive-location.model';

@Component({
  standalone: true,
  selector: 'jhi-hive-location-detail',
  templateUrl: './hive-location-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HiveLocationDetailComponent {
  hiveLocation = input<IHiveLocation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
