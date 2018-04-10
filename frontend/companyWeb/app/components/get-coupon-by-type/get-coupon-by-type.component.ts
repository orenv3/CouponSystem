import { CompanyRestService } from './../../services/company-rest.service';
import { Component, OnInit } from '@angular/core';
import { Coupon } from '../../common/Coupon';
import swal from 'sweetalert';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-get-coupon-by-type',
  templateUrl: './get-coupon-by-type.component.html',
  styleUrls: ['./get-coupon-by-type.component.css']
})
export class GetCouponByTypeComponent implements OnInit {

  public _coupons: Coupon[];
  constructor(private _webService: CompanyRestService) {

  }

  ngOnInit() {
    this.getCouponByType();
  }

  getCouponByType() {
    const self = this;
    this._coupons = new Array;
    return this._webService.getCouponByType().subscribe(
      function (result) {
        for (let cup of result) {
          cup = new Coupon(cup);
          cup.endDate = new Date(cup.endDate).toLocaleDateString();
          cup.startDate = new Date(cup.startDate).toLocaleDateString();
          self._coupons.push(cup);
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
