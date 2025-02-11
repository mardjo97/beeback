import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IQueen } from '../queen.model';
import { QueenService } from '../service/queen.service';
import { QueenFormService } from './queen-form.service';

import { QueenUpdateComponent } from './queen-update.component';

describe('Queen Management Update Component', () => {
  let comp: QueenUpdateComponent;
  let fixture: ComponentFixture<QueenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let queenFormService: QueenFormService;
  let queenService: QueenService;
  let userService: UserService;
  let hiveService: HiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QueenUpdateComponent],
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
      .overrideTemplate(QueenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QueenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    queenFormService = TestBed.inject(QueenFormService);
    queenService = TestBed.inject(QueenService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const queen: IQueen = { id: 456 };
      const user: IUser = { id: 14718 };
      queen.user = user;

      const userCollection: IUser[] = [{ id: 29348 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queen });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const queen: IQueen = { id: 456 };
      const hive: IHive = { id: 24012 };
      queen.hive = hive;

      const hiveCollection: IHive[] = [{ id: 12698 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queen });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const queen: IQueen = { id: 456 };
      const user: IUser = { id: 31268 };
      queen.user = user;
      const hive: IHive = { id: 32415 };
      queen.hive = hive;

      activatedRoute.data = of({ queen });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.queen).toEqual(queen);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueen>>();
      const queen = { id: 123 };
      jest.spyOn(queenFormService, 'getQueen').mockReturnValue(queen);
      jest.spyOn(queenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queen }));
      saveSubject.complete();

      // THEN
      expect(queenFormService.getQueen).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(queenService.update).toHaveBeenCalledWith(expect.objectContaining(queen));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueen>>();
      const queen = { id: 123 };
      jest.spyOn(queenFormService, 'getQueen').mockReturnValue({ id: null });
      jest.spyOn(queenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queen: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queen }));
      saveSubject.complete();

      // THEN
      expect(queenFormService.getQueen).toHaveBeenCalled();
      expect(queenService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueen>>();
      const queen = { id: 123 };
      jest.spyOn(queenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(queenService.update).toHaveBeenCalled();
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
