import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQueen } from '../queen.model';
import { QueenService } from '../service/queen.service';

@Component({
  standalone: true,
  templateUrl: './queen-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QueenDeleteDialogComponent {
  queen?: IQueen;

  protected queenService = inject(QueenService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.queenService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
