import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeedingHiveDetailComponent } from './feeding-hive-detail.component';

describe('FeedingHive Management Detail Component', () => {
  let comp: FeedingHiveDetailComponent;
  let fixture: ComponentFixture<FeedingHiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedingHiveDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./feeding-hive-detail.component').then(m => m.FeedingHiveDetailComponent),
              resolve: { feedingHive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FeedingHiveDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeedingHiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedingHive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FeedingHiveDetailComponent);

      // THEN
      expect(instance.feedingHive()).toEqual(expect.objectContaining({ id: 123 }));
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
