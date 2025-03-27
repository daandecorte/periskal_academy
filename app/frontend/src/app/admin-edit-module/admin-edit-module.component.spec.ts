import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditModuleComponent } from './admin-edit-module.component';

describe('AdminEditModuleComponent', () => {
  let component: AdminEditModuleComponent;
  let fixture: ComponentFixture<AdminEditModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEditModuleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEditModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
