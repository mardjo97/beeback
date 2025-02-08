import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IHiveType } from '../hive-type.model';

@Component({
  standalone: true,
  selector: 'jhi-hive-type-detail',
  templateUrl: './hive-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HiveTypeDetailComponent {
  hiveType = input<IHiveType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
