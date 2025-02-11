import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHiveLocation } from '../hive-location.model';
import { HiveLocationService } from '../service/hive-location.service';
import { HiveLocationFormGroup, HiveLocationFormService } from './hive-location-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hive-location-update',
  templateUrl: './hive-location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HiveLocationUpdateComponent implements OnInit {
  isSaving = false;
  hiveLocation: IHiveLocation | null = null;

  usersSharedCollection: IUser[] = [];

  protected hiveLocationService = inject(HiveLocationService);
  protected hiveLocationFormService = inject(HiveLocationFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HiveLocationFormGroup = this.hiveLocationFormService.createHiveLocationFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hiveLocation }) => {
      this.hiveLocation = hiveLocation;
      if (hiveLocation) {
        this.updateForm(hiveLocation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hiveLocation = this.hiveLocationFormService.getHiveLocation(this.editForm);
    if (hiveLocation.id !== null) {
      this.subscribeToSaveResponse(this.hiveLocationService.update(hiveLocation));
    } else {
      this.subscribeToSaveResponse(this.hiveLocationService.create(hiveLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHiveLocation>>): void {
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

  protected updateForm(hiveLocation: IHiveLocation): void {
    this.hiveLocation = hiveLocation;
    this.hiveLocationFormService.resetForm(this.editForm, hiveLocation);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, hiveLocation.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.hiveLocation?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
