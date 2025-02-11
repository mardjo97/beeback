import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GoodHarvestHiveDetailComponent } from './good-harvest-hive-detail.component';

describe('GoodHarvestHive Management Detail Component', () => {
  let comp: GoodHarvestHiveDetailComponent;
  let fixture: ComponentFixture<GoodHarvestHiveDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoodHarvestHiveDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./good-harvest-hive-detail.component').then(m => m.GoodHarvestHiveDetailComponent),
              resolve: { goodHarvestHive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GoodHarvestHiveDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GoodHarvestHiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load goodHarvestHive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GoodHarvestHiveDetailComponent);

      // THEN
      expect(instance.goodHarvestHive()).toEqual(expect.objectContaining({ id: 123 }));
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
