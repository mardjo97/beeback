import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeedingHive } from '../feeding-hive.model';
import { FeedingHiveService } from '../service/feeding-hive.service';

@Component({
  standalone: true,
  templateUrl: './feeding-hive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeedingHiveDeleteDialogComponent {
  feedingHive?: IFeedingHive;

  protected feedingHiveService = inject(FeedingHiveService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedingHiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
