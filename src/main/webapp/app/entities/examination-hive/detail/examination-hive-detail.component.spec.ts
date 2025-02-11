import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExaminationHiveDetailComponent } from './examination-hive-detail.component';

describe('ExaminationHive Management Detail Component', () => {
  let comp: ExaminationHiveDetailComponent;
  let fixture: ComponentFixture<ExaminationHiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExaminationHiveDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./examination-hive-detail.component').then(m => m.ExaminationHiveDetailComponent),
              resolve: { examinationHive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExaminationHiveDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExaminationHiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load examinationHive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExaminationHiveDetailComponent);

      // THEN
      expect(instance.examinationHive()).toEqual(expect.objectContaining({ id: 123 }));
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
