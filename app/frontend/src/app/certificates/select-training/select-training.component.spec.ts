import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectTrainingComponent } from './select-training.component';

describe('SelectModuleComponent', () => {
  let component: SelectTrainingComponent;
  let fixture: ComponentFixture<SelectTrainingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectTrainingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectTrainingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
