import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTrainingsComponent } from './edit-trainings.component';

describe('EditTrainingsComponent', () => {
  let component: EditTrainingsComponent;
  let fixture: ComponentFixture<EditTrainingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditTrainingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditTrainingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
