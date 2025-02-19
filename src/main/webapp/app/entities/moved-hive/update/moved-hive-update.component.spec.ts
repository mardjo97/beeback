import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';
import { HarvestTypeService } from 'app/entities/harvest-type/service/harvest-type.service';
import { IMovedHive } from '../moved-hive.model';
import { MovedHiveService } from '../service/moved-hive.service';
import { MovedHiveFormService } from './moved-hive-form.service';

import { MovedHiveUpdateComponent } from './moved-hive-update.component';

describe('MovedHive Management Update Component', () => {
  let comp: MovedHiveUpdateComponent;
  let fixture: ComponentFixture<MovedHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let movedHiveFormService: MovedHiveFormService;
  let movedHiveService: MovedHiveService;
  let userService: UserService;
  let hiveService: HiveService;
  let harvestTypeService: HarvestTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MovedHiveUpdateComponent],
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
      .overrideTemplate(MovedHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovedHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    movedHiveFormService = TestBed.inject(MovedHiveFormService);
    movedHiveService = TestBed.inject(MovedHiveService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);
    harvestTypeService = TestBed.inject(HarvestTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const movedHive: IMovedHive = { id: 456 };
      const user: IUser = { id: 28845 };
      movedHive.user = user;

      const userCollection: IUser[] = [{ id: 32383 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const movedHive: IMovedHive = { id: 456 };
      const hive: IHive = { id: 18320 };
      movedHive.hive = hive;

      const hiveCollection: IHive[] = [{ id: 21274 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HarvestType query and add missing value', () => {
      const movedHive: IMovedHive = { id: 456 };
      const harvestType: IHarvestType = { id: 23618 };
      movedHive.harvestType = harvestType;

      const harvestTypeCollection: IHarvestType[] = [{ id: 4144 }];
      jest.spyOn(harvestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: harvestTypeCollection })));
      const additionalHarvestTypes = [harvestType];
      const expectedCollection: IHarvestType[] = [...additionalHarvestTypes, ...harvestTypeCollection];
      jest.spyOn(harvestTypeService, 'addHarvestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      expect(harvestTypeService.query).toHaveBeenCalled();
      expect(harvestTypeService.addHarvestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        harvestTypeCollection,
        ...additionalHarvestTypes.map(expect.objectContaining),
      );
      expect(comp.harvestTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const movedHive: IMovedHive = { id: 456 };
      const user: IUser = { id: 10834 };
      movedHive.user = user;
      const hive: IHive = { id: 1710 };
      movedHive.hive = hive;
      const harvestType: IHarvestType = { id: 17675 };
      movedHive.harvestType = harvestType;

      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.harvestTypesSharedCollection).toContain(harvestType);
      expect(comp.movedHive).toEqual(movedHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovedHive>>();
      const movedHive = { id: 123 };
      jest.spyOn(movedHiveFormService, 'getMovedHive').mockReturnValue(movedHive);
      jest.spyOn(movedHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movedHive }));
      saveSubject.complete();

      // THEN
      expect(movedHiveFormService.getMovedHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(movedHiveService.update).toHaveBeenCalledWith(expect.objectContaining(movedHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovedHive>>();
      const movedHive = { id: 123 };
      jest.spyOn(movedHiveFormService, 'getMovedHive').mockReturnValue({ id: null });
      jest.spyOn(movedHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movedHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movedHive }));
      saveSubject.complete();

      // THEN
      expect(movedHiveFormService.getMovedHive).toHaveBeenCalled();
      expect(movedHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovedHive>>();
      const movedHive = { id: 123 };
      jest.spyOn(movedHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movedHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(movedHiveService.update).toHaveBeenCalled();
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

    describe('compareHarvestType', () => {
      it('Should forward to harvestTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(harvestTypeService, 'compareHarvestType');
        comp.compareHarvestType(entity, entity2);
        expect(harvestTypeService.compareHarvestType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
