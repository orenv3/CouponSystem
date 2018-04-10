import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAllPurchasedCouponsByTypeComponent } from './get-all-purchased-coupons-by-type.component';

describe('GetAllPurchasedCouponsByTypeComponent', () => {
  let component: GetAllPurchasedCouponsByTypeComponent;
  let fixture: ComponentFixture<GetAllPurchasedCouponsByTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetAllPurchasedCouponsByTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetAllPurchasedCouponsByTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
