import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipsAndTricksComponent } from './tips-and-tricks.component';

describe('TipsAndTricksComponent', () => {
  let component: TipsAndTricksComponent;
  let fixture: ComponentFixture<TipsAndTricksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipsAndTricksComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TipsAndTricksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
