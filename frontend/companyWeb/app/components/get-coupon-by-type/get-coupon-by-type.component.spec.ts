import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCouponByTypeComponent } from './get-coupon-by-type.component';

describe('GetCouponByTypeComponent', () => {
  let component: GetCouponByTypeComponent;
  let fixture: ComponentFixture<GetCouponByTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCouponByTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCouponByTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
