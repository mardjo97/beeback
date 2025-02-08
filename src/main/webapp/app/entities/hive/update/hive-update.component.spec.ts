import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHiveType } from 'app/entities/hive-type/hive-type.model';
import { HiveTypeService } from 'app/entities/hive-type/service/hive-type.service';
import { IApiary } from 'app/entities/apiary/apiary.model';
import { ApiaryService } from 'app/entities/apiary/service/apiary.service';
import { IHive } from '../hive.model';
import { HiveService } from '../service/hive.service';
import { HiveFormService } from './hive-form.service';

import { HiveUpdateComponent } from './hive-update.component';

describe('Hive Management Update Component', () => {
  let comp: HiveUpdateComponent;
  let fixture: ComponentFixture<HiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hiveFormService: HiveFormService;
  let hiveService: HiveService;
  let userService: UserService;
  let hiveTypeService: HiveTypeService;
  let apiaryService: ApiaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HiveUpdateComponent],
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
      .overrideTemplate(HiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hiveFormService = TestBed.inject(HiveFormService);
    hiveService = TestBed.inject(HiveService);
    userService = TestBed.inject(UserService);
    hiveTypeService = TestBed.inject(HiveTypeService);
    apiaryService = TestBed.inject(ApiaryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const hive: IHive = { id: 456 };
      const user: IUser = { id: 22813 };
      hive.user = user;

      const userCollection: IUser[] = [{ id: 31818 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HiveType query and add missing value', () => {
      const hive: IHive = { id: 456 };
      const hiveType: IHiveType = { id: 20395 };
      hive.hiveType = hiveType;

      const hiveTypeCollection: IHiveType[] = [{ id: 26883 }];
      jest.spyOn(hiveTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveTypeCollection })));
      const additionalHiveTypes = [hiveType];
      const expectedCollection: IHiveType[] = [...additionalHiveTypes, ...hiveTypeCollection];
      jest.spyOn(hiveTypeService, 'addHiveTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      expect(hiveTypeService.query).toHaveBeenCalled();
      expect(hiveTypeService.addHiveTypeToCollectionIfMissing).toHaveBeenCalledWith(
        hiveTypeCollection,
        ...additionalHiveTypes.map(expect.objectContaining),
      );
      expect(comp.hiveTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Apiary query and add missing value', () => {
      const hive: IHive = { id: 456 };
      const apiary: IApiary = { id: 18878 };
      hive.apiary = apiary;

      const apiaryCollection: IApiary[] = [{ id: 7051 }];
      jest.spyOn(apiaryService, 'query').mockReturnValue(of(new HttpResponse({ body: apiaryCollection })));
      const additionalApiaries = [apiary];
      const expectedCollection: IApiary[] = [...additionalApiaries, ...apiaryCollection];
      jest.spyOn(apiaryService, 'addApiaryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      expect(apiaryService.query).toHaveBeenCalled();
      expect(apiaryService.addApiaryToCollectionIfMissing).toHaveBeenCalledWith(
        apiaryCollection,
        ...additionalApiaries.map(expect.objectContaining),
      );
      expect(comp.apiariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hive: IHive = { id: 456 };
      const user: IUser = { id: 26593 };
      hive.user = user;
      const hiveType: IHiveType = { id: 69 };
      hive.hiveType = hiveType;
      const apiary: IApiary = { id: 1331 };
      hive.apiary = apiary;

      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hiveTypesSharedCollection).toContain(hiveType);
      expect(comp.apiariesSharedCollection).toContain(apiary);
      expect(comp.hive).toEqual(hive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHive>>();
      const hive = { id: 123 };
      jest.spyOn(hiveFormService, 'getHive').mockReturnValue(hive);
      jest.spyOn(hiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hive }));
      saveSubject.complete();

      // THEN
      expect(hiveFormService.getHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hiveService.update).toHaveBeenCalledWith(expect.objectContaining(hive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHive>>();
      const hive = { id: 123 };
      jest.spyOn(hiveFormService, 'getHive').mockReturnValue({ id: null });
      jest.spyOn(hiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hive }));
      saveSubject.complete();

      // THEN
      expect(hiveFormService.getHive).toHaveBeenCalled();
      expect(hiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHive>>();
      const hive = { id: 123 };
      jest.spyOn(hiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hiveService.update).toHaveBeenCalled();
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

    describe('compareHiveType', () => {
      it('Should forward to hiveTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hiveTypeService, 'compareHiveType');
        comp.compareHiveType(entity, entity2);
        expect(hiveTypeService.compareHiveType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApiary', () => {
      it('Should forward to apiaryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apiaryService, 'compareApiary');
        comp.compareApiary(entity, entity2);
        expect(apiaryService.compareApiary).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
