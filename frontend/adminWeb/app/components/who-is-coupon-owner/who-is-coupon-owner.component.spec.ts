import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WhoIsCouponOwnerComponent } from './who-is-coupon-owner.component';

describe('WhoIsCouponOwnerComponent', () => {
  let component: WhoIsCouponOwnerComponent;
  let fixture: ComponentFixture<WhoIsCouponOwnerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WhoIsCouponOwnerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhoIsCouponOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
