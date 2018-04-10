import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCouponByDateComponent } from './get-coupon-by-date.component';

describe('GetCouponByDateComponent', () => {
  let component: GetCouponByDateComponent;
  let fixture: ComponentFixture<GetCouponByDateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCouponByDateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCouponByDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
