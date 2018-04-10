import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCustomerProfileComponent } from './show-customer-profile.component';

describe('ShowCustomerProfileComponent', () => {
  let component: ShowCustomerProfileComponent;
  let fixture: ComponentFixture<ShowCustomerProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowCustomerProfileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCustomerProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
