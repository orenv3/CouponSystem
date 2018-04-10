import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Coupon } from './../../../common/Coupon';
import { CustomerRestService } from './../../services/customer-rest.service';
import swal from 'sweetalert';


@Component({
  selector: 'app-get-all-purchased-coupons-by-max-price',
  templateUrl: './get-all-purchased-coupons-by-max-price.component.html',
  styleUrls: ['./get-all-purchased-coupons-by-max-price.component.css']
})
export class GetAllPurchasedCouponsByMaxPriceComponent implements OnInit {
  public _coupons: Coupon[];
  public _maxPrice: number = 0;
  constructor(private _webService: CustomerRestService) {
    this.getAllCouponByPrice();
  }

  ngOnInit() {}

  getAllpurchasedCouponsByMaxPrice(_maxPrice) {
    this._coupons = new Array;
    const self = this;
    return this._webService.getAllpurchasedCouponsByMaxPrice(_maxPrice).subscribe(
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
          if (err.status == 400) {
            swal("please enter a price.", "", "error");
          } else {
            if (err.status == 500) {
              swal("The entered price is too low.", "There are no coupons to show till this price.", "info");
            } else {
              swal("Network or server problem", "please try again later", "error");
            }
          }
        }
      })
  }

  public getAllCouponByPrice() {
    const self = this;
    this._coupons = new Array;
    return this._webService.getAllpurchaseCouponsByPrice().subscribe(
      function (result) {
        for (let c of result) {
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


