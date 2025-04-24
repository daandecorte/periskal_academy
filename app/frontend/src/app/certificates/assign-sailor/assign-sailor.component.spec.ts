import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignSailorComponent } from './assign-sailor.component';

describe('AssignSailorComponent', () => {
  let component: AssignSailorComponent;
  let fixture: ComponentFixture<AssignSailorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignSailorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignSailorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
