import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApiary } from '../apiary.model';
import { ApiaryService } from '../service/apiary.service';

@Component({
  standalone: true,
  templateUrl: './apiary-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApiaryDeleteDialogComponent {
  apiary?: IApiary;

  protected apiaryService = inject(ApiaryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apiaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
