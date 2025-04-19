import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignedTrainingCardComponent } from './assigned-training-card.component';

describe('AssignedTrainingCardComponent', () => {
  let component: AssignedTrainingCardComponent;
  let fixture: ComponentFixture<AssignedTrainingCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignedTrainingCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignedTrainingCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
