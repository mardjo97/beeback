import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReproductionHive } from '../reproduction-hive.model';
import { ReproductionHiveService } from '../service/reproduction-hive.service';

@Component({
  standalone: true,
  templateUrl: './reproduction-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReproductionHiveDeleteDialogComponent {
  reproductionHive?: IReproductionHive;

  protected reproductionHiveService = inject(ReproductionHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reproductionHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
