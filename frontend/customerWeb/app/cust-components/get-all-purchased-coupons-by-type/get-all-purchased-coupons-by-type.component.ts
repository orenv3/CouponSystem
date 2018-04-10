import { Component, OnInit } from '@angular/core';
import { Coupon } from './../../../common/Coupon';
import { CustomerRestService } from './../../services/customer-rest.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-get-all-purchased-coupons-by-type',
  templateUrl: './get-all-purchased-coupons-by-type.component.html',
  styleUrls: ['./get-all-purchased-coupons-by-type.component.css']
})
export class GetAllPurchasedCouponsByTypeComponent implements OnInit {

  public _coupons: Coupon[];
  constructor(private _webService: CustomerRestService) {
    this.getAllpurchaseCouponsByType();
  }

  ngOnInit() {}

  getAllpurchaseCouponsByType() {
    var self = this;
    this._coupons = new Array;
    this._webService.getAllpurchaseCouponsByType().subscribe(
      function (coupons) {
        for (let c of coupons) {
          c = new Coupon(c);
          c.startDate = new Date(c.startDate).toDateString();
          c.endDate = new Date(c.endDate).toDateString();
          self._coupons.push(c);
        }

      },
      (err: HttpErrorResponse) => {
        if (err.error == Error) {
          swal("Something went wrong", "Please contact your manager", "error");
        } else {
          swal("Network or server problem", "please try again later", "error");
        }
      }
    )

  }

}

