import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainingExamComponent } from './training-exam.component';

describe('ExamComponent', () => {
  let component: TrainingExamComponent;
  let fixture: ComponentFixture<TrainingExamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrainingExamComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrainingExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
