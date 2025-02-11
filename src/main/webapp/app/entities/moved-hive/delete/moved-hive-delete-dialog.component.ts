import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMovedHive } from '../moved-hive.model';
import { MovedHiveService } from '../service/moved-hive.service';

@Component({
  standalone: true,
  templateUrl: './moved-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MovedHiveDeleteDialogComponent {
  movedHive?: IMovedHive;

  protected movedHiveService = inject(MovedHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.movedHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
