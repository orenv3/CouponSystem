import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCompanyComponent } from './get-company.component';

describe('GetCompanyComponent', () => {
  let component: GetCompanyComponent;
  let fixture: ComponentFixture<GetCompanyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCompanyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
