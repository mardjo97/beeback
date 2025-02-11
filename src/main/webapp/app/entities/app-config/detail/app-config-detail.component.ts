import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAppConfig } from '../app-config.model';

@Component({
  standalone: true,
  selector: 'jhi-app-config-detail',
  templateUrl: './app-config-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppConfigDetailComponent {
  appConfig = input<IAppConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
