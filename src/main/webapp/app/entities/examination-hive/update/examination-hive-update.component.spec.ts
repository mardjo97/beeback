import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IExaminationHive } from '../examination-hive.model';
import { ExaminationHiveService } from '../service/examination-hive.service';
import { ExaminationHiveFormService } from './examination-hive-form.service';

import { ExaminationHiveUpdateComponent } from './examination-hive-update.component';

describe('ExaminationHive Management Update Component', () => {
  let comp: ExaminationHiveUpdateComponent;
  let fixture: ComponentFixture<ExaminationHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let examinationHiveFormService: ExaminationHiveFormService;
  let examinationHiveService: ExaminationHiveService;
  let userService: UserService;
  let hiveService: HiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExaminationHiveUpdateComponent],
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
      .overrideTemplate(ExaminationHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExaminationHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    examinationHiveFormService = TestBed.inject(ExaminationHiveFormService);
    examinationHiveService = TestBed.inject(ExaminationHiveService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const examinationHive: IExaminationHive = { id: 456 };
      const user: IUser = { id: 22988 };
      examinationHive.user = user;

      const userCollection: IUser[] = [{ id: 25033 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examinationHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const examinationHive: IExaminationHive = { id: 456 };
      const hive: IHive = { id: 8233 };
      examinationHive.hive = hive;

      const hiveCollection: IHive[] = [{ id: 601 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examinationHive });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const examinationHive: IExaminationHive = { id: 456 };
      const user: IUser = { id: 18836 };
      examinationHive.user = user;
      const hive: IHive = { id: 903 };
      examinationHive.hive = hive;

      activatedRoute.data = of({ examinationHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.examinationHive).toEqual(examinationHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExaminationHive>>();
      const examinationHive = { id: 123 };
      jest.spyOn(examinationHiveFormService, 'getExaminationHive').mockReturnValue(examinationHive);
      jest.spyOn(examinationHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examinationHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: examinationHive }));
      saveSubject.complete();

      // THEN
      expect(examinationHiveFormService.getExaminationHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(examinationHiveService.update).toHaveBeenCalledWith(expect.objectContaining(examinationHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExaminationHive>>();
      const examinationHive = { id: 123 };
      jest.spyOn(examinationHiveFormService, 'getExaminationHive').mockReturnValue({ id: null });
      jest.spyOn(examinationHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examinationHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: examinationHive }));
      saveSubject.complete();

      // THEN
      expect(examinationHiveFormService.getExaminationHive).toHaveBeenCalled();
      expect(examinationHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExaminationHive>>();
      const examinationHive = { id: 123 };
      jest.spyOn(examinationHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examinationHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(examinationHiveService.update).toHaveBeenCalled();
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

    describe('compareHive', () => {
      it('Should forward to hiveService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hiveService, 'compareHive');
        comp.compareHive(entity, entity2);
        expect(hiveService.compareHive).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
