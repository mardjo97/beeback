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
import { GoodHarvestHiveService } from '../service/good-harvest-hive.service';
import { IGoodHarvestHive } from '../good-harvest-hive.model';
import { GoodHarvestHiveFormGroup, GoodHarvestHiveFormService } from './good-harvest-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-good-harvest-hive-update',
  templateUrl: './good-harvest-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GoodHarvestHiveUpdateComponent implements OnInit {
  isSaving = false;
  goodHarvestHive: IGoodHarvestHive | null = null;

  usersSharedCollection: IUser[] = [];
  harvestTypesSharedCollection: IHarvestType[] = [];

  protected goodHarvestHiveService = inject(GoodHarvestHiveService);
  protected goodHarvestHiveFormService = inject(GoodHarvestHiveFormService);
  protected userService = inject(UserService);
  protected harvestTypeService = inject(HarvestTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GoodHarvestHiveFormGroup = this.goodHarvestHiveFormService.createGoodHarvestHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHarvestType = (o1: IHarvestType | null, o2: IHarvestType | null): boolean => this.harvestTypeService.compareHarvestType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goodHarvestHive }) => {
      this.goodHarvestHive = goodHarvestHive;
      if (goodHarvestHive) {
        this.updateForm(goodHarvestHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goodHarvestHive = this.goodHarvestHiveFormService.getGoodHarvestHive(this.editForm);
    if (goodHarvestHive.id !== null) {
      this.subscribeToSaveResponse(this.goodHarvestHiveService.update(goodHarvestHive));
    } else {
      this.subscribeToSaveResponse(this.goodHarvestHiveService.create(goodHarvestHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodHarvestHive>>): void {
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

  protected updateForm(goodHarvestHive: IGoodHarvestHive): void {
    this.goodHarvestHive = goodHarvestHive;
    this.goodHarvestHiveFormService.resetForm(this.editForm, goodHarvestHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, goodHarvestHive.user);
    this.harvestTypesSharedCollection = this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(
      this.harvestTypesSharedCollection,
      goodHarvestHive.harvestType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.goodHarvestHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.harvestTypeService
      .query()
      .pipe(map((res: HttpResponse<IHarvestType[]>) => res.body ?? []))
      .pipe(
        map((harvestTypes: IHarvestType[]) =>
          this.harvestTypeService.addHarvestTypeToCollectionIfMissing<IHarvestType>(harvestTypes, this.goodHarvestHive?.harvestType),
        ),
      )
      .subscribe((harvestTypes: IHarvestType[]) => (this.harvestTypesSharedCollection = harvestTypes));
  }
}
