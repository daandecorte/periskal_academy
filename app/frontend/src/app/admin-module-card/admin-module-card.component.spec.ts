import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminModuleCardComponent } from './admin-module-card.component';

describe('AdminModuleCardComponent', () => {
  let component: AdminModuleCardComponent;
  let fixture: ComponentFixture<AdminModuleCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminModuleCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminModuleCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
