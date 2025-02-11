import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExaminationHive } from '../examination-hive.model';
import { ExaminationHiveService } from '../service/examination-hive.service';

@Component({
  standalone: true,
  templateUrl: './examination-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExaminationHiveDeleteDialogComponent {
  examinationHive?: IExaminationHive;

  protected examinationHiveService = inject(ExaminationHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.examinationHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
