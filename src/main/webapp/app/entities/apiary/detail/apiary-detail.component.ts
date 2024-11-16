import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IApiary } from '../apiary.model';

@Component({
  standalone: true,
  selector: 'jhi-apiary-detail',
  templateUrl: './apiary-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ApiaryDetailComponent {
  apiary = input<IApiary | null>(null);

  previousState(): void {
    window.history.back();
  }
}
