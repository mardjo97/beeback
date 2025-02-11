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
import { IHarvest } from '../harvest.model';
import { HarvestService } from '../service/harvest.service';
import { HarvestFormService } from './harvest-form.service';

import { HarvestUpdateComponent } from './harvest-update.component';

describe('Harvest Management Update Component', () => {
  let comp: HarvestUpdateComponent;
  let fixture: ComponentFixture<HarvestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let harvestFormService: HarvestFormService;
  let harvestService: HarvestService;
  let userService: UserService;
  let hiveService: HiveService;
  let harvestTypeService: HarvestTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HarvestUpdateComponent],
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
      .overrideTemplate(HarvestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HarvestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    harvestFormService = TestBed.inject(HarvestFormService);
    harvestService = TestBed.inject(HarvestService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);
    harvestTypeService = TestBed.inject(HarvestTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const harvest: IHarvest = { id: 456 };
      const user: IUser = { id: 32290 };
      harvest.user = user;

      const userCollection: IUser[] = [{ id: 12338 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const harvest: IHarvest = { id: 456 };
      const hive: IHive = { id: 16808 };
      harvest.hive = hive;

      const hiveCollection: IHive[] = [{ id: 13380 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HarvestType query and add missing value', () => {
      const harvest: IHarvest = { id: 456 };
      const harvestType: IHarvestType = { id: 7022 };
      harvest.harvestType = harvestType;

      const harvestTypeCollection: IHarvestType[] = [{ id: 14809 }];
      jest.spyOn(harvestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: harvestTypeCollection })));
      const additionalHarvestTypes = [harvestType];
      const expectedCollection: IHarvestType[] = [...additionalHarvestTypes, ...harvestTypeCollection];
      jest.spyOn(harvestTypeService, 'addHarvestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      expect(harvestTypeService.query).toHaveBeenCalled();
      expect(harvestTypeService.addHarvestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        harvestTypeCollection,
        ...additionalHarvestTypes.map(expect.objectContaining),
      );
      expect(comp.harvestTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const harvest: IHarvest = { id: 456 };
      const user: IUser = { id: 30241 };
      harvest.user = user;
      const hive: IHive = { id: 26294 };
      harvest.hive = hive;
      const harvestType: IHarvestType = { id: 5963 };
      harvest.harvestType = harvestType;

      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.harvestTypesSharedCollection).toContain(harvestType);
      expect(comp.harvest).toEqual(harvest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvest>>();
      const harvest = { id: 123 };
      jest.spyOn(harvestFormService, 'getHarvest').mockReturnValue(harvest);
      jest.spyOn(harvestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: harvest }));
      saveSubject.complete();

      // THEN
      expect(harvestFormService.getHarvest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(harvestService.update).toHaveBeenCalledWith(expect.objectContaining(harvest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvest>>();
      const harvest = { id: 123 };
      jest.spyOn(harvestFormService, 'getHarvest').mockReturnValue({ id: null });
      jest.spyOn(harvestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: harvest }));
      saveSubject.complete();

      // THEN
      expect(harvestFormService.getHarvest).toHaveBeenCalled();
      expect(harvestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvest>>();
      const harvest = { id: 123 };
      jest.spyOn(harvestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(harvestService.update).toHaveBeenCalled();
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
