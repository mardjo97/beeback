import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHiveLocation } from '../hive-location.model';
import { HiveLocationService } from '../service/hive-location.service';

@Component({
  standalone: true,
  templateUrl: './hive-location-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HiveLocationDeleteDialogComponent {
  hiveLocation?: IHiveLocation;

  protected hiveLocationService = inject(HiveLocationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hiveLocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
