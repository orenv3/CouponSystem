import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCouponByIdComponent } from './get-coupon-by-id.component';

describe('GetCouponByIdComponent', () => {
  let component: GetCouponByIdComponent;
  let fixture: ComponentFixture<GetCouponByIdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCouponByIdComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCouponByIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
