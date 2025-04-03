import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraineeChatComponent } from './trainee-chat.component';

describe('TraineeChatComponent', () => {
  let component: TraineeChatComponent;
  let fixture: ComponentFixture<TraineeChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TraineeChatComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraineeChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
