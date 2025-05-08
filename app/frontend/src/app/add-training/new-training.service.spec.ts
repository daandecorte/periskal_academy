import { TestBed } from '@angular/core/testing';

import { NewTrainingService } from '../add-training/new-training.service';

describe('NewTrainingService', () => {
  let service: NewTrainingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NewTrainingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
