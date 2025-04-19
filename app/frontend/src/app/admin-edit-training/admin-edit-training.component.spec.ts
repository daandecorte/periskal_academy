import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditTrainingComponent } from './admin-edit-training.component';

describe('AdminEditTrainingComponent', () => {
  let component: AdminEditTrainingComponent;
  let fixture: ComponentFixture<AdminEditTrainingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEditTrainingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEditTrainingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
