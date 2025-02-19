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
import { QueenChangeHiveService } from '../service/queen-change-hive.service';
import { IQueenChangeHive } from '../queen-change-hive.model';
import { QueenChangeHiveFormGroup, QueenChangeHiveFormService } from './queen-change-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-queen-change-hive-update',
  templateUrl: './queen-change-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QueenChangeHiveUpdateComponent implements OnInit {
  isSaving = false;
  queenChangeHive: IQueenChangeHive | null = null;

  usersSharedCollection: IUser[] = [];
  hivesSharedCollection: IHive[] = [];

  protected queenChangeHiveService = inject(QueenChangeHiveService);
  protected queenChangeHiveFormService = inject(QueenChangeHiveFormService);
  protected userService = inject(UserService);
  protected hiveService = inject(HiveService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QueenChangeHiveFormGroup = this.queenChangeHiveFormService.createQueenChangeHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHive = (o1: IHive | null, o2: IHive | null): boolean => this.hiveService.compareHive(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ queenChangeHive }) => {
      this.queenChangeHive = queenChangeHive;
      if (queenChangeHive) {
        this.updateForm(queenChangeHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const queenChangeHive = this.queenChangeHiveFormService.getQueenChangeHive(this.editForm);
    if (queenChangeHive.id !== null) {
      this.subscribeToSaveResponse(this.queenChangeHiveService.update(queenChangeHive));
    } else {
      this.subscribeToSaveResponse(this.queenChangeHiveService.create(queenChangeHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQueenChangeHive>>): void {
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

  protected updateForm(queenChangeHive: IQueenChangeHive): void {
    this.queenChangeHive = queenChangeHive;
    this.queenChangeHiveFormService.resetForm(this.editForm, queenChangeHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, queenChangeHive.user);
    this.hivesSharedCollection = this.hiveService.addHiveToCollectionIfMissing<IHive>(this.hivesSharedCollection, queenChangeHive.hive);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.queenChangeHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveService
      .query()
      .pipe(map((res: HttpResponse<IHive[]>) => res.body ?? []))
      .pipe(map((hives: IHive[]) => this.hiveService.addHiveToCollectionIfMissing<IHive>(hives, this.queenChangeHive?.hive)))
      .subscribe((hives: IHive[]) => (this.hivesSharedCollection = hives));
  }
}
