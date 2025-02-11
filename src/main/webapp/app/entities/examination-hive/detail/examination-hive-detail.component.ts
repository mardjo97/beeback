import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IExaminationHive } from '../examination-hive.model';

@Component({
  standalone: true,
  selector: 'jhi-examination-hive-detail',
  templateUrl: './examination-hive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExaminationHiveDetailComponent {
  examinationHive = input<IExaminationHive | null>(null);

  previousState(): void {
    window.history.back();
  }
}
