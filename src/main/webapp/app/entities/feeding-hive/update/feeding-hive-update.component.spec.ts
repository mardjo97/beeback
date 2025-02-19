import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHive } from 'app/entities/hive/hive.model';
import { HiveService } from 'app/entities/hive/service/hive.service';
import { IFeedingHive } from '../feeding-hive.model';
import { FeedingHiveService } from '../service/feeding-hive.service';
import { FeedingHiveFormService } from './feeding-hive-form.service';

import { FeedingHiveUpdateComponent } from './feeding-hive-update.component';

describe('FeedingHive Management Update Component', () => {
  let comp: FeedingHiveUpdateComponent;
  let fixture: ComponentFixture<FeedingHiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedingHiveFormService: FeedingHiveFormService;
  let feedingHiveService: FeedingHiveService;
  let userService: UserService;
  let hiveService: HiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedingHiveUpdateComponent],
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
      .overrideTemplate(FeedingHiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedingHiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedingHiveFormService = TestBed.inject(FeedingHiveFormService);
    feedingHiveService = TestBed.inject(FeedingHiveService);
    userService = TestBed.inject(UserService);
    hiveService = TestBed.inject(HiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const feedingHive: IFeedingHive = { id: 456 };
      const user: IUser = { id: 3697 };
      feedingHive.user = user;

      const userCollection: IUser[] = [{ id: 25130 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedingHive });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hive query and add missing value', () => {
      const feedingHive: IFeedingHive = { id: 456 };
      const hive: IHive = { id: 25522 };
      feedingHive.hive = hive;

      const hiveCollection: IHive[] = [{ id: 4768 }];
      jest.spyOn(hiveService, 'query').mockReturnValue(of(new HttpResponse({ body: hiveCollection })));
      const additionalHives = [hive];
      const expectedCollection: IHive[] = [...additionalHives, ...hiveCollection];
      jest.spyOn(hiveService, 'addHiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedingHive });
      comp.ngOnInit();

      expect(hiveService.query).toHaveBeenCalled();
      expect(hiveService.addHiveToCollectionIfMissing).toHaveBeenCalledWith(
        hiveCollection,
        ...additionalHives.map(expect.objectContaining),
      );
      expect(comp.hivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedingHive: IFeedingHive = { id: 456 };
      const user: IUser = { id: 25674 };
      feedingHive.user = user;
      const hive: IHive = { id: 13212 };
      feedingHive.hive = hive;

      activatedRoute.data = of({ feedingHive });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.hivesSharedCollection).toContain(hive);
      expect(comp.feedingHive).toEqual(feedingHive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedingHive>>();
      const feedingHive = { id: 123 };
      jest.spyOn(feedingHiveFormService, 'getFeedingHive').mockReturnValue(feedingHive);
      jest.spyOn(feedingHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedingHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedingHive }));
      saveSubject.complete();

      // THEN
      expect(feedingHiveFormService.getFeedingHive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedingHiveService.update).toHaveBeenCalledWith(expect.objectContaining(feedingHive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedingHive>>();
      const feedingHive = { id: 123 };
      jest.spyOn(feedingHiveFormService, 'getFeedingHive').mockReturnValue({ id: null });
      jest.spyOn(feedingHiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedingHive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedingHive }));
      saveSubject.complete();

      // THEN
      expect(feedingHiveFormService.getFeedingHive).toHaveBeenCalled();
      expect(feedingHiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedingHive>>();
      const feedingHive = { id: 123 };
      jest.spyOn(feedingHiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedingHive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedingHiveService.update).toHaveBeenCalled();
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
