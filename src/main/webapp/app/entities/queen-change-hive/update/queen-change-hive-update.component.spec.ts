import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IQueenChangeHive } from '../queen-change-hive.model';
import { QueenChangeHiveService } from '../service/queen-change-hive.service';
import { QueenChangeHiveFormService } from './queen-change-hive-form.service';

import { QueenChangeHiveUpdateComponent } from './queen-change-hive-update.component';

describe('QueenChangeHive Management Update Component', () => {
  let comp: QueenChangeHiveUpdateComponent;
  let fixture: ComponentFixture<QueenChangeHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let queenChangeHiveFormService: QueenChangeHiveFormService;
  let queenChangeHiveService: QueenChangeHiveService;
  let userService: UserService;
  let hiveService: HiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QueenChangeHiveUpdateComponent],
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
      .overrideTemplate(QueenChangeHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QueenChangeHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    queenChangeHiveFormService = TestBed.inject(QueenChangeHiveFormService);
    queenChangeHiveService = TestBed.inject(QueenChangeHiveService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const queenChangeHive: IQueenChangeHive = { id: 456 };
      const user: IUser = { id: 10063 };
      queenChangeHive.user = user;

      const userCollection: IUser[] = [{ id: 930 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queenChangeHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const queenChangeHive: IQueenChangeHive = { id: 456 };
      const hive: IHive = { id: 6765 };
      queenChangeHive.hive = hive;

      const hiveCollection: IHive[] = [{ id: 23552 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queenChangeHive });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const queenChangeHive: IQueenChangeHive = { id: 456 };
      const user: IUser = { id: 32524 };
      queenChangeHive.user = user;
      const hive: IHive = { id: 15308 };
      queenChangeHive.hive = hive;

      activatedRoute.data = of({ queenChangeHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.queenChangeHive).toEqual(queenChangeHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueenChangeHive>>();
      const queenChangeHive = { id: 123 };
      jest.spyOn(queenChangeHiveFormService, 'getQueenChangeHive').mockReturnValue(queenChangeHive);
      jest.spyOn(queenChangeHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queenChangeHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queenChangeHive }));
      saveSubject.complete();

      // THEN
      expect(queenChangeHiveFormService.getQueenChangeHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(queenChangeHiveService.update).toHaveBeenCalledWith(expect.objectContaining(queenChangeHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueenChangeHive>>();
      const queenChangeHive = { id: 123 };
      jest.spyOn(queenChangeHiveFormService, 'getQueenChangeHive').mockReturnValue({ id: null });
      jest.spyOn(queenChangeHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queenChangeHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queenChangeHive }));
      saveSubject.complete();

      // THEN
      expect(queenChangeHiveFormService.getQueenChangeHive).toHaveBeenCalled();
      expect(queenChangeHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueenChangeHive>>();
      const queenChangeHive = { id: 123 };
      jest.spyOn(queenChangeHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queenChangeHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(queenChangeHiveService.update).toHaveBeenCalled();
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
