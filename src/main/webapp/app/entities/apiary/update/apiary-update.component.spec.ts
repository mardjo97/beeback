import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ApiaryService } from '../service/apiary.service';
import { IApiary } from '../apiary.model';
import { ApiaryFormService } from './apiary-form.service';

import { ApiaryUpdateComponent } from './apiary-update.component';

describe('Apiary Management Update Component', () => {
  let comp: ApiaryUpdateComponent;
  let fixture: ComponentFixture<ApiaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apiaryFormService: ApiaryFormService;
  let apiaryService: ApiaryService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApiaryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApiaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApiaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apiaryFormService = TestBed.inject(ApiaryFormService);
    apiaryService = TestBed.inject(ApiaryService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const apiary: IApiary = { id: 456 };
      const user: IUser = { id: 20046 };
      apiary.user = user;

      const userCollection: IUser[] = [{ id: 19568 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apiary });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apiary: IApiary = { id: 456 };
      const user: IUser = { id: 14518 };
      apiary.user = user;

      activatedRoute.data = of({ apiary });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.apiary).toEqual(apiary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApiary>>();
      const apiary = { id: 123 };
      jest.spyOn(apiaryFormService, 'getApiary').mockReturnValue(apiary);
      jest.spyOn(apiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apiary }));
      saveSubject.complete();

      // THEN
      expect(apiaryFormService.getApiary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apiaryService.update).toHaveBeenCalledWith(expect.objectContaining(apiary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApiary>>();
      const apiary = { id: 123 };
      jest.spyOn(apiaryFormService, 'getApiary').mockReturnValue({ id: null });
      jest.spyOn(apiaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apiary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apiary }));
      saveSubject.complete();

      // THEN
      expect(apiaryFormService.getApiary).toHaveBeenCalled();
      expect(apiaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApiary>>();
      const apiary = { id: 123 };
      jest.spyOn(apiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apiaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
