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
import { ReproductionHiveService } from '../service/reproduction-hive.service';
import { IReproductionHive } from '../reproduction-hive.model';
import { ReproductionHiveFormGroup, ReproductionHiveFormService } from './reproduction-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reproduction-hive-update',
  templateUrl: './reproduction-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReproductionHiveUpdateComponent implements OnInit {
  isSaving = false;
  reproductionHive: IReproductionHive | null = null;

  usersSharedCollection: IUser[] = [];
  hivesSharedCollection: IHive[] = [];

  protected reproductionHiveService = inject(ReproductionHiveService);
  protected reproductionHiveFormService = inject(ReproductionHiveFormService);
  protected userService = inject(UserService);
  protected hiveService = inject(HiveService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReproductionHiveFormGroup = this.reproductionHiveFormService.createReproductionHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHive = (o1: IHive | null, o2: IHive | null): boolean => this.hiveService.compareHive(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reproductionHive }) => {
      this.reproductionHive = reproductionHive;
      if (reproductionHive) {
        this.updateForm(reproductionHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reproductionHive = this.reproductionHiveFormService.getReproductionHive(this.editForm);
    if (reproductionHive.id !== null) {
      this.subscribeToSaveResponse(this.reproductionHiveService.update(reproductionHive));
    } else {
      this.subscribeToSaveResponse(this.reproductionHiveService.create(reproductionHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReproductionHive>>): void {
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

  protected updateForm(reproductionHive: IReproductionHive): void {
    this.reproductionHive = reproductionHive;
    this.reproductionHiveFormService.resetForm(this.editForm, reproductionHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, reproductionHive.user);
    this.hivesSharedCollection = this.hiveService.addHiveToCollectionIfMissing<IHive>(this.hivesSharedCollection, reproductionHive.hive);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.reproductionHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveService
      .query()
      .pipe(map((res: HttpResponse<IHive[]>) => res.body ?? []))
      .pipe(map((hives: IHive[]) => this.hiveService.addHiveToCollectionIfMissing<IHive>(hives, this.reproductionHive?.hive)))
      .subscribe((hives: IHive[]) => (this.hivesSharedCollection = hives));
  }
}
