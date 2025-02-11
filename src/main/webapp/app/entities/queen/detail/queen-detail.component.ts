import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IQueen } from '../queen.model';

@Component({
  standalone: true,
  selector: 'jhi-queen-detail',
  templateUrl: './queen-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QueenDetailComponent {
  queen = input<IQueen | null>(null);

  previousState(): void {
    window.history.back();
  }
}
