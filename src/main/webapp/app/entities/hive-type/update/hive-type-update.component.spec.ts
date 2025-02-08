import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { HiveTypeService } from '../service/hive-type.service';
import { IHiveType } from '../hive-type.model';
import { HiveTypeFormService } from './hive-type-form.service';

import { HiveTypeUpdateComponent } from './hive-type-update.component';

describe('HiveType Management Update Component', () => {
  let comp: HiveTypeUpdateComponent;
  let fixture: ComponentFixture<HiveTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hiveTypeFormService: HiveTypeFormService;
  let hiveTypeService: HiveTypeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HiveTypeUpdateComponent],
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
      .overrideTemplate(HiveTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HiveTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hiveTypeFormService = TestBed.inject(HiveTypeFormService);
    hiveTypeService = TestBed.inject(HiveTypeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const hiveType: IHiveType = { id: 456 };
      const user: IUser = { id: 4747 };
      hiveType.user = user;

      const userCollection: IUser[] = [{ id: 15658 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hiveType });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hiveType: IHiveType = { id: 456 };
      const user: IUser = { id: 21274 };
      hiveType.user = user;

      activatedRoute.data = of({ hiveType });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hiveType).toEqual(hiveType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveType>>();
      const hiveType = { id: 123 };
      jest.spyOn(hiveTypeFormService, 'getHiveType').mockReturnValue(hiveType);
      jest.spyOn(hiveTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hiveType }));
      saveSubject.complete();

      // THEN
      expect(hiveTypeFormService.getHiveType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hiveTypeService.update).toHaveBeenCalledWith(expect.objectContaining(hiveType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveType>>();
      const hiveType = { id: 123 };
      jest.spyOn(hiveTypeFormService, 'getHiveType').mockReturnValue({ id: null });
      jest.spyOn(hiveTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hiveType }));
      saveSubject.complete();

      // THEN
      expect(hiveTypeFormService.getHiveType).toHaveBeenCalled();
      expect(hiveTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveType>>();
      const hiveType = { id: 123 };
      jest.spyOn(hiveTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hiveTypeService.update).toHaveBeenCalled();
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
