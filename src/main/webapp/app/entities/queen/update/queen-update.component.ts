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
import { QueenService } from '../service/queen.service';
import { IQueen } from '../queen.model';
import { QueenFormGroup, QueenFormService } from './queen-form.service';

@Component({
  standalone: true,
  selector: 'jhi-queen-update',
  templateUrl: './queen-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QueenUpdateComponent implements OnInit {
  isSaving = false;
  queen: IQueen | null = null;

  usersSharedCollection: IUser[] = [];
  hivesSharedCollection: IHive[] = [];

  protected queenService = inject(QueenService);
  protected queenFormService = inject(QueenFormService);
  protected userService = inject(UserService);
  protected hiveService = inject(HiveService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QueenFormGroup = this.queenFormService.createQueenFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHive = (o1: IHive | null, o2: IHive | null): boolean => this.hiveService.compareHive(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ queen }) => {
      this.queen = queen;
      if (queen) {
        this.updateForm(queen);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const queen = this.queenFormService.getQueen(this.editForm);
    if (queen.id !== null) {
      this.subscribeToSaveResponse(this.queenService.update(queen));
    } else {
      this.subscribeToSaveResponse(this.queenService.create(queen));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQueen>>): void {
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

  protected updateForm(queen: IQueen): void {
    this.queen = queen;
    this.queenFormService.resetForm(this.editForm, queen);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, queen.user);
    this.hivesSharedCollection = this.hiveService.addHiveToCollectionIfMissing<IHive>(this.hivesSharedCollection, queen.hive);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.queen?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveService
      .query()
      .pipe(map((res: HttpResponse<IHive[]>) => res.body ?? []))
      .pipe(map((hives: IHive[]) => this.hiveService.addHiveToCollectionIfMissing<IHive>(hives, this.queen?.hive)))
      .subscribe((hives: IHive[]) => (this.hivesSharedCollection = hives));
  }
}
