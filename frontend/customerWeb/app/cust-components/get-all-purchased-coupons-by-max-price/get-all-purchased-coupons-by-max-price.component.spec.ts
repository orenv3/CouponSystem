import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAllPurchasedCouponsByMaxPriceComponent } from './get-all-purchased-coupons-by-max-price.component';

describe('GetAllPurchasedCouponsByMaxPriceComponent', () => {
  let component: GetAllPurchasedCouponsByMaxPriceComponent;
  let fixture: ComponentFixture<GetAllPurchasedCouponsByMaxPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetAllPurchasedCouponsByMaxPriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetAllPurchasedCouponsByMaxPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
