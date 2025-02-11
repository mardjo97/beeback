import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IExaminationHive } from '../examination-hive.model';
import { ExaminationHiveService } from '../service/examination-hive.service';
import { ExaminationHiveFormGroup, ExaminationHiveFormService } from './examination-hive-form.service';

@Component({
  standalone: true,
  selector: 'jhi-examination-hive-update',
  templateUrl: './examination-hive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExaminationHiveUpdateComponent implements OnInit {
  isSaving = false;
  examinationHive: IExaminationHive | null = null;

  usersSharedCollection: IUser[] = [];

  protected examinationHiveService = inject(ExaminationHiveService);
  protected examinationHiveFormService = inject(ExaminationHiveFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExaminationHiveFormGroup = this.examinationHiveFormService.createExaminationHiveFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ examinationHive }) => {
      this.examinationHive = examinationHive;
      if (examinationHive) {
        this.updateForm(examinationHive);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const examinationHive = this.examinationHiveFormService.getExaminationHive(this.editForm);
    if (examinationHive.id !== null) {
      this.subscribeToSaveResponse(this.examinationHiveService.update(examinationHive));
    } else {
      this.subscribeToSaveResponse(this.examinationHiveService.create(examinationHive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExaminationHive>>): void {
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

  protected updateForm(examinationHive: IExaminationHive): void {
    this.examinationHive = examinationHive;
    this.examinationHiveFormService.resetForm(this.editForm, examinationHive);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, examinationHive.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.examinationHive?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
