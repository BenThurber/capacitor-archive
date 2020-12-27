import { TestBed } from '@angular/core/testing';

import { DynamicRouterService } from './dynamic-router.service';
import {RouterModule} from '@angular/router';

describe('DynamicRouterService', () => {
  let service: DynamicRouterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterModule.forRoot([], { relativeLinkResolution: 'legacy' }),]
    });
    service = TestBed.inject(DynamicRouterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
