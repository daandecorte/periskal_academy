import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignedModuleCardComponent } from './assigned-module-card.component';

describe('AssignedModuleCardComponent', () => {
  let component: AssignedModuleCardComponent;
  let fixture: ComponentFixture<AssignedModuleCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignedModuleCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignedModuleCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
