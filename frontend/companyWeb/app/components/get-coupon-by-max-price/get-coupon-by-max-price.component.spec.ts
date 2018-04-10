import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCouponByMaxPriceComponent } from './get-coupon-by-max-price.component';

describe('GetCouponByMaxPriceComponent', () => {
  let component: GetCouponByMaxPriceComponent;
  let fixture: ComponentFixture<GetCouponByMaxPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCouponByMaxPriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCouponByMaxPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
