import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHive } from '../hive.model';
import { HiveService } from '../service/hive.service';

@Component({
  standalone: true,
  templateUrl: './hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HiveDeleteDialogComponent {
  hive?: IHive;

  protected hiveService = inject(HiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
