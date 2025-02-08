import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHiveType } from '../hive-type.model';
import { HiveTypeService } from '../service/hive-type.service';
import { HiveTypeFormGroup, HiveTypeFormService } from './hive-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hive-type-update',
  templateUrl: './hive-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HiveTypeUpdateComponent implements OnInit {
  isSaving = false;
  hiveType: IHiveType | null = null;

  usersSharedCollection: IUser[] = [];

  protected hiveTypeService = inject(HiveTypeService);
  protected hiveTypeFormService = inject(HiveTypeFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HiveTypeFormGroup = this.hiveTypeFormService.createHiveTypeFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hiveType }) => {
      this.hiveType = hiveType;
      if (hiveType) {
        this.updateForm(hiveType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hiveType = this.hiveTypeFormService.getHiveType(this.editForm);
    if (hiveType.id !== null) {
      this.subscribeToSaveResponse(this.hiveTypeService.update(hiveType));
    } else {
      this.subscribeToSaveResponse(this.hiveTypeService.create(hiveType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHiveType>>): void {
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

  protected updateForm(hiveType: IHiveType): void {
    this.hiveType = hiveType;
    this.hiveTypeFormService.resetForm(this.editForm, hiveType);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, hiveType.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.hiveType?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
