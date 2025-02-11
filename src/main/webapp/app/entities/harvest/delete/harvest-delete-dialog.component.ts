import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHarvest } from '../harvest.model';
import { HarvestService } from '../service/harvest.service';

@Component({
  standalone: true,
  templateUrl: './harvest-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HarvestDeleteDialogComponent {
  harvest?: IHarvest;

  protected harvestService = inject(HarvestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.harvestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
