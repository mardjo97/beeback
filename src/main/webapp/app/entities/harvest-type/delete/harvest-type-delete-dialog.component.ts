import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHarvestType } from '../harvest-type.model';
import { HarvestTypeService } from '../service/harvest-type.service';

@Component({
  standalone: true,
  templateUrl: './harvest-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HarvestTypeDeleteDialogComponent {
  harvestType?: IHarvestType;

  protected harvestTypeService = inject(HarvestTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.harvestTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
