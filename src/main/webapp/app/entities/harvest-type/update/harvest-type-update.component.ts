import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHarvestType } from '../harvest-type.model';
import { HarvestTypeService } from '../service/harvest-type.service';
import { HarvestTypeFormGroup, HarvestTypeFormService } from './harvest-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-harvest-type-update',
  templateUrl: './harvest-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HarvestTypeUpdateComponent implements OnInit {
  isSaving = false;
  harvestType: IHarvestType | null = null;

  usersSharedCollection: IUser[] = [];

  protected harvestTypeService = inject(HarvestTypeService);
  protected harvestTypeFormService = inject(HarvestTypeFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HarvestTypeFormGroup = this.harvestTypeFormService.createHarvestTypeFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ harvestType }) => {
      this.harvestType = harvestType;
      if (harvestType) {
        this.updateForm(harvestType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const harvestType = this.harvestTypeFormService.getHarvestType(this.editForm);
    if (harvestType.id !== null) {
      this.subscribeToSaveResponse(this.harvestTypeService.update(harvestType));
    } else {
      this.subscribeToSaveResponse(this.harvestTypeService.create(harvestType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHarvestType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(harvestType: IHarvestType): void {
    this.harvestType = harvestType;
    this.harvestTypeFormService.resetForm(this.editForm, harvestType);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, harvestType.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.harvestType?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
