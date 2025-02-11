import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQueenChangeHive } from '../queen-change-hive.model';
import { QueenChangeHiveService } from '../service/queen-change-hive.service';

@Component({
  standalone: true,
  templateUrl: './queen-change-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QueenChangeHiveDeleteDialogComponent {
  queenChangeHive?: IQueenChangeHive;

  protected queenChangeHiveService = inject(QueenChangeHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.queenChangeHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
