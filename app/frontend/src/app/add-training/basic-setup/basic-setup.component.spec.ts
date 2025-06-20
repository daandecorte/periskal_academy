import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicSetupComponent } from './basic-setup.component';

describe('BasicSetupComponent', () => {
  let component: BasicSetupComponent;
  let fixture: ComponentFixture<BasicSetupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BasicSetupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BasicSetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
