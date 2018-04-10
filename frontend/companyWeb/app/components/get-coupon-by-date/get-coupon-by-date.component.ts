import { HttpErrorResponse } from '@angular/common/http';
import { CompanyRestService } from './../../services/company-rest.service';
import { Component, OnInit } from '@angular/core';
import { Coupon } from '../../common/Coupon';
import swal from 'sweetalert';

@Component({
  selector: 'app-get-coupon-by-date',
  templateUrl: './get-coupon-by-date.component.html',
  styleUrls: ['./get-coupon-by-date.component.css']
})
export class GetCouponByDateComponent implements OnInit {

  public _coupons: Coupon[];
  public _date: Date;
  constructor(private _webService: CompanyRestService) {}

  ngOnInit() {}


  getCouponByDate() {
    const self = this;
    this._coupons = new Array;
    return this._webService.getCouponByDate(this._date).subscribe(
      function (result) {
        for (let c of result) {

          c = new Coupon(c);
          c.startDate = new Date(c.startDate).toLocaleDateString();
          c.endDate = new Date(c.endDate).toLocaleDateString();
          self._coupons.push(c);
        }
        if (self._coupons.length == 0) {
          swal("No coupons were found within this range.", "", "info");
        }
      },
      (err: HttpErrorResponse) => {
        if (err instanceof Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          if (err.status == 500) {
            swal("Entering a date value is mandatory", "Note: the date must not be in the past.", "error");
          } else {
            swal("Network or server problem", "please try again later", "error");
          }
        }
      }
    )
  }

}

