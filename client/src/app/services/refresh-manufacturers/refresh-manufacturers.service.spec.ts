import { TestBed } from '@angular/core/testing';

import { RefreshManufacturersService } from './refresh-manufacturers.service';

describe('RefreshManufacturersService', () => {
  let service: RefreshManufacturersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RefreshManufacturersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
