import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { QueenChangeHiveDetailComponent } from './queen-change-hive-detail.component';

describe('QueenChangeHive Management Detail Component', () => {
  let comp: QueenChangeHiveDetailComponent;
  let fixture: ComponentFixture<QueenChangeHiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QueenChangeHiveDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./queen-change-hive-detail.component').then(m => m.QueenChangeHiveDetailComponent),
              resolve: { queenChangeHive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QueenChangeHiveDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QueenChangeHiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load queenChangeHive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QueenChangeHiveDetailComponent);

      // THEN
      expect(instance.queenChangeHive()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
