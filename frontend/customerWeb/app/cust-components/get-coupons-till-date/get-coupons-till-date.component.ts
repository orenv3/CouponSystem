import { CustomerRestService } from './../../services/customer-rest.service';
import { Coupon } from './../../../common/Coupon';
import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-coupons-till-date',
  templateUrl: './get-coupons-till-date.component.html',
  styleUrls: ['./get-coupons-till-date.component.css']
})
export class GetCouponsTillDateComponent implements OnInit {

  public _coupons: Coupon[];
  public coupon: Coupon = new Coupon();
  public _date: Date;
  constructor(private _webService: CustomerRestService) {}

  ngOnInit() {}

  getCouponsTillDate() {
    this._coupons = new Array;
    const self = this;
    this._webService.getCouponsTillDate(this._date).subscribe(
      function (response) {
        for (let cp of response) {
          self.coupon = new Coupon(cp);
          self.coupon.startDate = new Date(cp.startDate).toDateString();
          self.coupon.endDate = new Date(cp.endDate).toDateString();
          self._coupons.push(self.coupon);
        }
        if (self._coupons.length == 0) {
          swal("There are no coupons within this range", "please enter a date in the future", "info")
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 500) {
            swal("Entering a date value is mandatory!", "please enter a date.", "error");
          } else
            swal("Network or server problem", "please try again later", "error");
        }
      }

    )

  }

}

