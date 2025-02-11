import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGoodHarvestHive } from '../good-harvest-hive.model';
import { GoodHarvestHiveService } from '../service/good-harvest-hive.service';

@Component({
  standalone: true,
  templateUrl: './good-harvest-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GoodHarvestHiveDeleteDialogComponent {
  goodHarvestHive?: IGoodHarvestHive;

  protected goodHarvestHiveService = inject(GoodHarvestHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.goodHarvestHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
