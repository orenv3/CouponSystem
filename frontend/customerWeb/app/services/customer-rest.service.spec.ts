import { TestBed, inject } from '@angular/core/testing';

import { CustomerRestService } from './customer-rest.service';

describe('CustomerRestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CustomerRestService]
    });
  });

  it('should be created', inject([CustomerRestService], (service: CustomerRestService) => {
    expect(service).toBeTruthy();
  }));
});
