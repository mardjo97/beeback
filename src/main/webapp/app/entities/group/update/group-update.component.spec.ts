import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { GroupService } from '../service/group.service';
import { IGroup } from '../group.model';
import { GroupFormService } from './group-form.service';

import { GroupUpdateComponent } from './group-update.component';

describe('Group Management Update Component', () => {
  let comp: GroupUpdateComponent;
  let fixture: ComponentFixture<GroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let groupFormService: GroupFormService;
  let groupService: GroupService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GroupUpdateComponent],
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
      .overrideTemplate(GroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    groupFormService = TestBed.inject(GroupFormService);
    groupService = TestBed.inject(GroupService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const group: IGroup = { id: 456 };
      const user: IUser = { id: 23095 };
      group.user = user;

      const userCollection: IUser[] = [{ id: 7132 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ group });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const group: IGroup = { id: 456 };
      const user: IUser = { id: 32557 };
      group.user = user;

      activatedRoute.data = of({ group });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.group).toEqual(group);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroup>>();
      const group = { id: 123 };
      jest.spyOn(groupFormService, 'getGroup').mockReturnValue(group);
      jest.spyOn(groupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ group });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: group }));
      saveSubject.complete();

      // THEN
      expect(groupFormService.getGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(groupService.update).toHaveBeenCalledWith(expect.objectContaining(group));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroup>>();
      const group = { id: 123 };
      jest.spyOn(groupFormService, 'getGroup').mockReturnValue({ id: null });
      jest.spyOn(groupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ group: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: group }));
      saveSubject.complete();

      // THEN
      expect(groupFormService.getGroup).toHaveBeenCalled();
      expect(groupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroup>>();
      const group = { id: 123 };
      jest.spyOn(groupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ group });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(groupService.update).toHaveBeenCalled();
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
