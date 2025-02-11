import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';
import { HarvestTypeService } from 'app/entities/harvest-type/service/harvest-type.service';
import { MovedHiveService } from '../service/moved-hive.service';
import { IMovedHive } from '../moved-hive.model';
import { MovedHiveFormGroup, MovedHiveFormService } from './moved-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-moved-hive-update',
  templateUrl: './moved-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MovedHiveUpdateComponent implements OnInit {
  isSaving = false;
  movedHive: IMovedHive | null = null;

  usersSharedCollection: IUser[] = [];
  harvestTypesSharedCollection: IHarvestType[] = [];

  protected movedHiveService = inject(MovedHiveService);
  protected movedHiveFormService = inject(MovedHiveFormService);
  protected userService = inject(UserService);
  protected harvestTypeService = inject(HarvestTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MovedHiveFormGroup = this.movedHiveFormService.createMovedHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHarvestType = (o1: IHarvestType | null, o2: IHarvestType | null): boolean => this.harvestTypeService.compareHarvestType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movedHive }) => {
      this.movedHive = movedHive;
      if (movedHive) {
        this.updateForm(movedHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movedHive = this.movedHiveFormService.getMovedHive(this.editForm);
    if (movedHive.id !== null) {
      this.subscribeToSaveResponse(this.movedHiveService.update(movedHive));
    } else {
      this.subscribeToSaveResponse(this.movedHiveService.create(movedHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovedHive>>): void {
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

  protected updateForm(movedHive: IMovedHive): void {
    this.movedHive = movedHive;
    this.movedHiveFormService.resetForm(this.editForm, movedHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, movedHive.user);
    this.harvestTypesSharedCollection = this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(
      this.harvestTypesSharedCollection,
      movedHive.harvestType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.movedHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.harvestTypeService
      .query()
      .pipe(map((res: HttpResponse<IHarvestType[]>) => res.body ?? []))
      .pipe(
        map((harvestTypes: IHarvestType[]) =>
          this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(harvestTypes, this.movedHive?.harvestType),
        ),
      )
      .subscribe((harvestTypes: IHarvestType[]) => (this.harvestTypesSharedCollection = harvestTypes));
  }
}
