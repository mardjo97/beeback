import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IGoodHarvestHive } from '../good-harvest-hive.model';

@Component({
  standalone: true,
  selector: 'jhi-good-harvest-hive-detail',
  templateUrl: './good-harvest-hive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GoodHarvestHiveDetailComponent {
  goodHarvestHive = input<IGoodHarvestHive | null>(null);

  previousState(): void {
    window.history.back();
  }
}
