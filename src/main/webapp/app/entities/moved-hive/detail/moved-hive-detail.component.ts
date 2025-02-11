import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMovedHive } from '../moved-hive.model';

@Component({
  standalone: true,
  selector: 'jhi-moved-hive-detail',
  templateUrl: './moved-hive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MovedHiveDetailComponent {
  movedHive = input<IMovedHive | null>(null);

  previousState(): void {
    window.history.back();
  }
}
