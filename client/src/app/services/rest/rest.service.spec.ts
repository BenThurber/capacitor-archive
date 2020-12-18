import { TestBed } from '@angular/core/testing';

import { RestService } from './rest.service';

describe('RestService', () => {
  let service: RestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestService);
  });

  // This needs fixing
  // it('should be created', () => {
  //   expect(service).toBeTruthy();
  // });
});
