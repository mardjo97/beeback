import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';
import { HarvestTypeService } from 'app/entities/harvest-type/service/harvest-type.service';
import { HarvestService } from '../service/harvest.service';
import { IHarvest } from '../harvest.model';
import { HarvestFormGroup, HarvestFormService } from './harvest-form.service';

@Component({
  standalone: true,
  selector: 'jhi-harvest-update',
  templateUrl: './harvest-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HarvestUpdateComponent implements OnInit {
  isSaving = false;
  harvest: IHarvest | null = null;

  usersSharedCollection: IUser[] = [];
  hivesSharedCollection: IHive[] = [];
  harvestTypesSharedCollection: IHarvestType[] = [];

  protected harvestService = inject(HarvestService);
  protected harvestFormService = inject(HarvestFormService);
  protected userService = inject(UserService);
  protected hiveService = inject(HiveService);
  protected harvestTypeService = inject(HarvestTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HarvestFormGroup = this.harvestFormService.createHarvestFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHive = (o1: IHive | null, o2: IHive | null): boolean => this.hiveService.compareHive(o1, o2);

  compareHarvestType = (o1: IHarvestType | null, o2: IHarvestType | null): boolean => this.harvestTypeService.compareHarvestType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ harvest }) => {
      this.harvest = harvest;
      if (harvest) {
        this.updateForm(harvest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const harvest = this.harvestFormService.getHarvest(this.editForm);
    if (harvest.id !== null) {
      this.subscribeToSaveResponse(this.harvestService.update(harvest));
    } else {
      this.subscribeToSaveResponse(this.harvestService.create(harvest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHarvest>>): void {
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

  protected updateForm(harvest: IHarvest): void {
    this.harvest = harvest;
    this.harvestFormService.resetForm(this.editForm, harvest);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, harvest.user);
    this.hivesSharedCollection = this.hiveService.addHiveToCollectionIfMissing<IHive>(this.hivesSharedCollection, harvest.hive);
    this.harvestTypesSharedCollection = this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(
      this.harvestTypesSharedCollection,
      harvest.harvestType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.harvest?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveService
      .query()
      .pipe(map((res: HttpResponse<IHive[]>) => res.body ?? []))
      .pipe(map((hives: IHive[]) => this.hiveService.addHiveToCollectionIfMissing<IHive>(hives, this.harvest?.hive)))
      .subscribe((hives: IHive[]) => (this.hivesSharedCollection = hives));

    this.harvestTypeService
      .query()
      .pipe(map((res: HttpResponse<IHarvestType[]>) => res.body ?? []))
      .pipe(
        map((harvestTypes: IHarvestType[]) =>
          this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(harvestTypes, this.harvest?.harvestType),
        ),
      )
      .subscribe((harvestTypes: IHarvestType[]) => (this.harvestTypesSharedCollection = harvestTypes));
  }
}
