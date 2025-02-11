import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { HarvestTypeService } from '../service/harvest-type.service';
import { IHarvestType } from '../harvest-type.model';
import { HarvestTypeFormService } from './harvest-type-form.service';

import { HarvestTypeUpdateComponent } from './harvest-type-update.component';

describe('HarvestType Management Update Component', () => {
  let comp: HarvestTypeUpdateComponent;
  let fixture: ComponentFixture<HarvestTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let harvestTypeFormService: HarvestTypeFormService;
  let harvestTypeService: HarvestTypeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HarvestTypeUpdateComponent],
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
      .overrideTemplate(HarvestTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HarvestTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    harvestTypeFormService = TestBed.inject(HarvestTypeFormService);
    harvestTypeService = TestBed.inject(HarvestTypeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const harvestType: IHarvestType = { id: 456 };
      const user: IUser = { id: 28550 };
      harvestType.user = user;

      const userCollection: IUser[] = [{ id: 10148 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ harvestType });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const harvestType: IHarvestType = { id: 456 };
      const user: IUser = { id: 25139 };
      harvestType.user = user;

      activatedRoute.data = of({ harvestType });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.harvestType).toEqual(harvestType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvestType>>();
      const harvestType = { id: 123 };
      jest.spyOn(harvestTypeFormService, 'getHarvestType').mockReturnValue(harvestType);
      jest.spyOn(harvestTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: harvestType }));
      saveSubject.complete();

      // THEN
      expect(harvestTypeFormService.getHarvestType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(harvestTypeService.update).toHaveBeenCalledWith(expect.objectContaining(harvestType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvestType>>();
      const harvestType = { id: 123 };
      jest.spyOn(harvestTypeFormService, 'getHarvestType').mockReturnValue({ id: null });
      jest.spyOn(harvestTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvestType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: harvestType }));
      saveSubject.complete();

      // THEN
      expect(harvestTypeFormService.getHarvestType).toHaveBeenCalled();
      expect(harvestTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHarvestType>>();
      const harvestType = { id: 123 };
      jest.spyOn(harvestTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ harvestType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(harvestTypeService.update).toHaveBeenCalled();
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
