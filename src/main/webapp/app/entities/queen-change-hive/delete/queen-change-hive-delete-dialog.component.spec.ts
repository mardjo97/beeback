jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { QueenChangeHiveService } from '../service/queen-change-hive.service';

import { QueenChangeHiveDeleteDialogComponent } from './queen-change-hive-delete-dialog.component';

describe('QueenChangeHive Management Delete Component', () => {
  let comp: QueenChangeHiveDeleteDialogComponent;
  let fixture: ComponentFixture<QueenChangeHiveDeleteDialogComponent>;
  let service: QueenChangeHiveService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QueenChangeHiveDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(QueenChangeHiveDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QueenChangeHiveDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QueenChangeHiveService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
