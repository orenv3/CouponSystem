import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCouponsTillDateComponent } from './get-coupons-till-date.component';

describe('GetCouponsTillDateComponent', () => {
  let component: GetCouponsTillDateComponent;
  let fixture: ComponentFixture<GetCouponsTillDateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetCouponsTillDateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetCouponsTillDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
