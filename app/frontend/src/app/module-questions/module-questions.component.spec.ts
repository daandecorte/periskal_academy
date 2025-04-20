import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModuleQuestionsComponent } from './module-questions.component';

describe('QuestionsComponent', () => {
  let component: ModuleQuestionsComponent;
  let fixture: ComponentFixture<ModuleQuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModuleQuestionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModuleQuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
