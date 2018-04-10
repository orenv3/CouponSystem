import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WhoIsPurchasedTheCouponComponent } from './who-is-purchased-the-coupon.component';

describe('WhoIsPurchasedTheCouponComponent', () => {
  let component: WhoIsPurchasedTheCouponComponent;
  let fixture: ComponentFixture<WhoIsPurchasedTheCouponComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WhoIsPurchasedTheCouponComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhoIsPurchasedTheCouponComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
