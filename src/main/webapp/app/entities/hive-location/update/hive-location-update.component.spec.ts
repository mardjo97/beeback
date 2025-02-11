import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { HiveLocationService } from '../service/hive-location.service';
import { IHiveLocation } from '../hive-location.model';
import { HiveLocationFormService } from './hive-location-form.service';

import { HiveLocationUpdateComponent } from './hive-location-update.component';

describe('HiveLocation Management Update Component', () => {
  let comp: HiveLocationUpdateComponent;
  let fixture: ComponentFixture<HiveLocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hiveLocationFormService: HiveLocationFormService;
  let hiveLocationService: HiveLocationService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HiveLocationUpdateComponent],
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
      .overrideTemplate(HiveLocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HiveLocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hiveLocationFormService = TestBed.inject(HiveLocationFormService);
    hiveLocationService = TestBed.inject(HiveLocationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const hiveLocation: IHiveLocation = { id: 456 };
      const user: IUser = { id: 5497 };
      hiveLocation.user = user;

      const userCollection: IUser[] = [{ id: 13104 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hiveLocation });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hiveLocation: IHiveLocation = { id: 456 };
      const user: IUser = { id: 17436 };
      hiveLocation.user = user;

      activatedRoute.data = of({ hiveLocation });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hiveLocation).toEqual(hiveLocation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveLocation>>();
      const hiveLocation = { id: 123 };
      jest.spyOn(hiveLocationFormService, 'getHiveLocation').mockReturnValue(hiveLocation);
      jest.spyOn(hiveLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hiveLocation }));
      saveSubject.complete();

      // THEN
      expect(hiveLocationFormService.getHiveLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hiveLocationService.update).toHaveBeenCalledWith(expect.objectContaining(hiveLocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveLocation>>();
      const hiveLocation = { id: 123 };
      jest.spyOn(hiveLocationFormService, 'getHiveLocation').mockReturnValue({ id: null });
      jest.spyOn(hiveLocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hiveLocation }));
      saveSubject.complete();

      // THEN
      expect(hiveLocationFormService.getHiveLocation).toHaveBeenCalled();
      expect(hiveLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHiveLocation>>();
      const hiveLocation = { id: 123 };
      jest.spyOn(hiveLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hiveLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hiveLocationService.update).toHaveBeenCalled();
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
