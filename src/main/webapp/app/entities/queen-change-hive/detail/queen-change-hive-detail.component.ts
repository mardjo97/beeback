import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IQueenChangeHive } from '../queen-change-hive.model';

@Component({
  standalone: true,
  selector: 'jhi-queen-change-hive-detail',
  templateUrl: './queen-change-hive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QueenChangeHiveDetailComponent {
  queenChangeHive = input<IQueenChangeHive | null>(null);

  previousState(): void {
    window.history.back();
  }
}
