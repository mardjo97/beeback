import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MovedHiveDetailComponent } from './moved-hive-detail.component';

describe('MovedHive Management Detail Component', () => {
  let comp: MovedHiveDetailComponent;
  let fixture: ComponentFixture<MovedHiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovedHiveDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./moved-hive-detail.component').then(m => m.MovedHiveDetailComponent),
              resolve: { movedHive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MovedHiveDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MovedHiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load movedHive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MovedHiveDetailComponent);

      // THEN
      expect(instance.movedHive()).toEqual(expect.objectContaining({ id: 123 }));
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
