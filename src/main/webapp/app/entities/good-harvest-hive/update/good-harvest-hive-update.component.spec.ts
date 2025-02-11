import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHarvestType } from 'app/entities/harvest-type/harvest-type.model';
import { HarvestTypeService } from 'app/entities/harvest-type/service/harvest-type.service';
import { IGoodHarvestHive } from '../good-harvest-hive.model';
import { GoodHarvestHiveService } from '../service/good-harvest-hive.service';
import { GoodHarvestHiveFormService } from './good-harvest-hive-form.service';

import { GoodHarvestHiveUpdateComponent } from './good-harvest-hive-update.component';

describe('GoodHarvestHive Management Update Component', () => {
  let comp: GoodHarvestHiveUpdateComponent;
  let fixture: ComponentFixture<GoodHarvestHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let goodHarvestHiveFormService: GoodHarvestHiveFormService;
  let goodHarvestHiveService: GoodHarvestHiveService;
  let userService: UserService;
  let harvestTypeService: HarvestTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GoodHarvestHiveUpdateComponent],
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
      .overrideTemplate(GoodHarvestHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GoodHarvestHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    goodHarvestHiveFormService = TestBed.inject(GoodHarvestHiveFormService);
    goodHarvestHiveService = TestBed.inject(GoodHarvestHiveService);
    userService = TestBed.inject(UserService);
    harvestTypeService = TestBed.inject(HarvestTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const goodHarvestHive: IGoodHarvestHive = { id: 456 };
      const user: IUser = { id: 15556 };
      goodHarvestHive.user = user;

      const userCollection: IUser[] = [{ id: 30538 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ goodHarvestHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HarvestType query and add missing value', () => {
      const goodHarvestHive: IGoodHarvestHive = { id: 456 };
      const harvestType: IHarvestType = { id: 16863 };
      goodHarvestHive.harvestType = harvestType;

      const harvestTypeCollection: IHarvestType[] = [{ id: 20802 }];
      jest.spyOn(harvestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: harvestTypeCollection })));
      const additionalHarvestTypes = [harvestType];
      const expectedCollection: IHarvestType[] = [...additionalHarvestTypes, ...harvestTypeCollection];
      jest.spyOn(harvestTypeService, 'addHarvestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ goodHarvestHive });
      comp.ngOnInit();

      expect(harvestTypeService.query).toHaveBeenCalled();
      expect(harvestTypeService.addHarvestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        harvestTypeCollection,
        ...additionalHarvestTypes.map(expect.objectContaining),
      );
      expect(comp.harvestTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const goodHarvestHive: IGoodHarvestHive = { id: 456 };
      const user: IUser = { id: 23932 };
      goodHarvestHive.user = user;
      const harvestType: IHarvestType = { id: 16544 };
      goodHarvestHive.harvestType = harvestType;

      activatedRoute.data = of({ goodHarvestHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.harvestTypesSharedCollection).toContain(harvestType);
      expect(comp.goodHarvestHive).toEqual(goodHarvestHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGoodHarvestHive>>();
      const goodHarvestHive = { id: 123 };
      jest.spyOn(goodHarvestHiveFormService, 'getGoodHarvestHive').mockReturnValue(goodHarvestHive);
      jest.spyOn(goodHarvestHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodHarvestHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goodHarvestHive }));
      saveSubject.complete();

      // THEN
      expect(goodHarvestHiveFormService.getGoodHarvestHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(goodHarvestHiveService.update).toHaveBeenCalledWith(expect.objectContaining(goodHarvestHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGoodHarvestHive>>();
      const goodHarvestHive = { id: 123 };
      jest.spyOn(goodHarvestHiveFormService, 'getGoodHarvestHive').mockReturnValue({ id: null });
      jest.spyOn(goodHarvestHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodHarvestHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goodHarvestHive }));
      saveSubject.complete();

      // THEN
      expect(goodHarvestHiveFormService.getGoodHarvestHive).toHaveBeenCalled();
      expect(goodHarvestHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGoodHarvestHive>>();
      const goodHarvestHive = { id: 123 };
      jest.spyOn(goodHarvestHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodHarvestHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(goodHarvestHiveService.update).toHaveBeenCalled();
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
