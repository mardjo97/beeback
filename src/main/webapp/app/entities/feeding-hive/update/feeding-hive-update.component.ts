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
import { FeedingHiveService } from '../service/feeding-hive.service';
import { IFeedingHive } from '../feeding-hive.model';
import { FeedingHiveFormGroup, FeedingHiveFormService } from './feeding-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feeding-hive-update',
  templateUrl: './feeding-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedingHiveUpdateComponent implements OnInit {
  isSaving = false;
  feedingHive: IFeedingHive | null = null;

  usersSharedCollection: IUser[] = [];
  hivesSharedCollection: IHive[] = [];

  protected feedingHiveService = inject(FeedingHiveService);
  protected feedingHiveFormService = inject(FeedingHiveFormService);
  protected userService = inject(UserService);
  protected hiveService = inject(HiveService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedingHiveFormGroup = this.feedingHiveFormService.createFeedingHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareHive = (o1: IHive | null, o2: IHive | null): boolean => this.hiveService.compareHive(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedingHive }) => {
      this.feedingHive = feedingHive;
      if (feedingHive) {
        this.updateForm(feedingHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedingHive = this.feedingHiveFormService.getFeedingHive(this.editForm);
    if (feedingHive.id !== null) {
      this.subscribeToSaveResponse(this.feedingHiveService.update(feedingHive));
    } else {
      this.subscribeToSaveResponse(this.feedingHiveService.create(feedingHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedingHive>>): void {
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

  protected updateForm(feedingHive: IFeedingHive): void {
    this.feedingHive = feedingHive;
    this.feedingHiveFormService.resetForm(this.editForm, feedingHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, feedingHive.user);
    this.hivesSharedCollection = this.hiveService.addHiveToCollectionIfMissing<IHive>(this.hivesSharedCollection, feedingHive.hive);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.feedingHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.hiveService
      .query()
      .pipe(map((res: HttpResponse<IHive[]>) => res.body ?? []))
      .pipe(map((hives: IHive[]) => this.hiveService.addHiveToCollectionIfMissing<IHive>(hives, this.feedingHive?.hive)))
      .subscribe((hives: IHive[]) => (this.hivesSharedCollection = hives));
  }
}
