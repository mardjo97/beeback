import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IApiary } from '../apiary.model';
import { ApiaryService } from '../service/apiary.service';
import { ApiaryFormGroup, ApiaryFormService } from './apiary-form.service';

@Component({
  standalone: true,
  selector: 'jhi-apiary-update',
  templateUrl: './apiary-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApiaryUpdateComponent implements OnInit {
  isSaving = false;
  apiary: IApiary | null = null;

  usersSharedCollection: IUser[] = [];

  protected apiaryService = inject(ApiaryService);
  protected apiaryFormService = inject(ApiaryFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ApiaryFormGroup = this.apiaryFormService.createApiaryFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apiary }) => {
      this.apiary = apiary;
      if (apiary) {
        this.updateForm(apiary);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apiary = this.apiaryFormService.getApiary(this.editForm);
    if (apiary.id !== null) {
      this.subscribeToSaveResponse(this.apiaryService.update(apiary));
    } else {
      this.subscribeToSaveResponse(this.apiaryService.create(apiary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApiary>>): void {
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

  protected updateForm(apiary: IApiary): void {
    this.apiary = apiary;
    this.apiaryFormService.resetForm(this.editForm, apiary);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, apiary.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.apiary?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
