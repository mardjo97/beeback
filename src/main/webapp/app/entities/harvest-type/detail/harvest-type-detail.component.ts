import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IHarvestType } from '../harvest-type.model';

@Component({
  standalone: true,
  selector: 'jhi-harvest-type-detail',
  templateUrl: './harvest-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HarvestTypeDetailComponent {
  harvestType = input<IHarvestType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
