import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ReproductionHiveService } from '../service/reproduction-hive.service';
import { IReproductionHive } from '../reproduction-hive.model';
import { ReproductionHiveFormService } from './reproduction-hive-form.service';

import { ReproductionHiveUpdateComponent } from './reproduction-hive-update.component';

describe('ReproductionHive Management Update Component', () => {
  let comp: ReproductionHiveUpdateComponent;
  let fixture: ComponentFixture<ReproductionHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reproductionHiveFormService: ReproductionHiveFormService;
  let reproductionHiveService: ReproductionHiveService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReproductionHiveUpdateComponent],
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
      .overrideTemplate(ReproductionHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReproductionHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reproductionHiveFormService = TestBed.inject(ReproductionHiveFormService);
    reproductionHiveService = TestBed.inject(ReproductionHiveService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const reproductionHive: IReproductionHive = { id: 456 };
      const user: IUser = { id: 995 };
      reproductionHive.user = user;

      const userCollection: IUser[] = [{ id: 30064 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reproductionHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reproductionHive: IReproductionHive = { id: 456 };
      const user: IUser = { id: 32732 };
      reproductionHive.user = user;

      activatedRoute.data = of({ reproductionHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.reproductionHive).toEqual(reproductionHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReproductionHive>>();
      const reproductionHive = { id: 123 };
      jest.spyOn(reproductionHiveFormService, 'getReproductionHive').mockReturnValue(reproductionHive);
      jest.spyOn(reproductionHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reproductionHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reproductionHive }));
      saveSubject.complete();

      // THEN
      expect(reproductionHiveFormService.getReproductionHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reproductionHiveService.update).toHaveBeenCalledWith(expect.objectContaining(reproductionHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReproductionHive>>();
      const reproductionHive = { id: 123 };
      jest.spyOn(reproductionHiveFormService, 'getReproductionHive').mockReturnValue({ id: null });
      jest.spyOn(reproductionHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reproductionHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reproductionHive }));
      saveSubject.complete();

      // THEN
      expect(reproductionHiveFormService.getReproductionHive).toHaveBeenCalled();
      expect(reproductionHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReproductionHive>>();
      const reproductionHive = { id: 123 };
      jest.spyOn(reproductionHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reproductionHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reproductionHiveService.update).toHaveBeenCalled();
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
