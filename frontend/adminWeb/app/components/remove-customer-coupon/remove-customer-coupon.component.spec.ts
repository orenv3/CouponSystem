import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveCustomerCouponComponent } from './remove-customer-coupon.component';

describe('RemoveCustomerCouponComponent', () => {
  let component: RemoveCustomerCouponComponent;
  let fixture: ComponentFixture<RemoveCustomerCouponComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoveCustomerCouponComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveCustomerCouponComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
