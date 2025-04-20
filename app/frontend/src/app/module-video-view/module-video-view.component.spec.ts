import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModuleVideoViewComponent } from './module-video-view.component';

describe('ModuleVideoViewComponent', () => {
  let component: ModuleVideoViewComponent;
  let fixture: ComponentFixture<ModuleVideoViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModuleVideoViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModuleVideoViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
