import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFeedingHive } from '../feeding-hive.model';

@Component({
  standalone: true,
  selector: 'jhi-feeding-hive-detail',
  templateUrl: './feeding-hive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeedingHiveDetailComponent {
  feedingHive = input<IFeedingHive | null>(null);

  previousState(): void {
    window.history.back();
  }
}
