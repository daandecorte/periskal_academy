import { TestBed } from '@angular/core/testing';

import { TrainingEditService } from './training-edit.service';

describe('TrainingEditService', () => {
  let service: TrainingEditService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrainingEditService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
