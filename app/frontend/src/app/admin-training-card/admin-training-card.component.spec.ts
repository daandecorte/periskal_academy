import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTrainingCardComponent } from './admin-training-card.component';

describe('AdminTrainingCardComponent', () => {
  let component: AdminTrainingCardComponent;
  let fixture: ComponentFixture<AdminTrainingCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTrainingCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTrainingCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
