import { TestBed } from '@angular/core/testing';

import { ExamService } from './exam.service';

describe('ExamServiceService', () => {
  let service: ExamService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
