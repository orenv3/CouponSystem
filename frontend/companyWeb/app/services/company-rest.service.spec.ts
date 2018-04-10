import { TestBed, inject } from '@angular/core/testing';

import { CompanyRestService } from './company-rest.service';

describe('CompanyRestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CompanyRestService]
    });
  });

  it('should be created', inject([CompanyRestService], (service: CompanyRestService) => {
    expect(service).toBeTruthy();
  }));
});
