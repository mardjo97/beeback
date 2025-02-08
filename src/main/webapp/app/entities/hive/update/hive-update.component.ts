import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHiveType } from 'app/entities/hive-type/hive-type.model';
import { HiveTypeService } from 'app/entities/hive-type/service/hive-type.service';
import { IApiary } from 'app/entities/apiary/apiary.model';
import { ApiaryService } from 'app/entities/apiary/service/apiary.service';
import { HiveService } from '../service/hive.service';
import { IHive } from '../hive.model';
import { HiveFormGroup, HiveFormService } from './hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hive-update',
  templateUrl: './hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HiveUpdateComponent implements OnInit {
  isSaving = false;
  hive: IHive | null = null;

  usersSharedCollection: IUser[] = [];
  hiveTypesSharedCollection: IHiveType[] = [];
  apiariesSharedCollection: IApiary[] = [];

  protected hiveService = inject(HiveService);
  protected hiveFormService = inject(HiveFormService);
  protected userService = inject(UserService);
  protected hiveTypeService = inject(HiveTypeService);
  protected apiaryService = inject(ApiaryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HiveFormGroup = this.hiveFormService.createHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHiveType = (o1: IHiveType | null, o2: IHiveType | null): boolean => this.hiveTypeService.compareHiveType(o1, o2);

  compareApiary = (o1: IApiary | null, o2: IApiary | null): boolean => this.apiaryService.compareApiary(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hive }) => {
      this.hive = hive;
      if (hive) {
        this.updateForm(hive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hive = this.hiveFormService.getHive(this.editForm);
    if (hive.id !== null) {
      this.subscribeToSaveResponse(this.hiveService.update(hive));
    } else {
      this.subscribeToSaveResponse(this.hiveService.create(hive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHive>>): void {
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

  protected updateForm(hive: IHive): void {
    this.hive = hive;
    this.hiveFormService.resetForm(this.editForm, hive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, hive.user);
    this.hiveTypesSharedCollection = this.hiveTypeService.addHiveTypeToCollectionIfMissing<IHiveType>(
      this.hiveTypesSharedCollection,
      hive.hiveType,
    );
    this.apiariesSharedCollection = this.apiaryService.addApiaryToCollectionIfMissing<IApiary>(this.apiariesSharedCollection, hive.apiary);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.hive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveTypeService
      .query()
      .pipe(map((res: HttpResponse<IHiveType[]>) => res.body ?? []))
      .pipe(
        map((hiveTypes: IHiveType[]) => this.hiveTypeService.addHiveTypeToCollectionIfMissing<IHiveType>(hiveTypes, this.hive?.hiveType)),
      )
      .subscribe((hiveTypes: IHiveType[]) => (this.hiveTypesSharedCollection = hiveTypes));

    this.apiaryService
      .query()
      .pipe(map((res: HttpResponse<IApiary[]>) => res.body ?? []))
      .pipe(map((apiaries: IApiary[]) => this.apiaryService.addApiaryToCollectionIfMissing<IApiary>(apiaries, this.hive?.apiary)))
      .subscribe((apiaries: IApiary[]) => (this.apiariesSharedCollection = apiaries));
  }
}
